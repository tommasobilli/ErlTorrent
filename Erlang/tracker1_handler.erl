-module(tracker1_handler).
-behavior(cowboy_handler).

-export([init/2,
		allowed_methods/2,
		content_types_provided/2,
		content_types_accepted/2,
		get_help_request/2,
		handle_get_request/2,
        handle_post_request/2,
        post_user_request/2,
        get_user_request/2
    ]).

-record(state, {op}).


pick([N, SL]) ->
    Result = random(SL,N),
    Result.

random(L, N) ->
    random(list_to_tuple(L), N, []).

random({},_,Acc) ->
    Acc;
random(_,0,Acc) ->
    Acc;
random(T, N, Acc) ->
    Index = rand:uniform(size(T)),
    Elem = element(Index, T),
    random(delete_element(Index, T), N-1, Acc ++ [Elem]).

delete_element(N,T) -> 
    L = erlang:tuple_to_list(T),
    Ele = erlang:element(N, T),
    erlang:list_to_tuple(lists:filter(fun(X) -> X =/= Ele end, L)). 

init(Req, Opts) ->
    [Op | _] = Opts,
    State = #state{op=Op},
    {cowboy_rest, Req, State}.

allowed_methods(Req, State) ->
    Methods = [<<"GET">>, <<"POST">>],
    {Methods, Req, State}.

content_types_provided(Req, State) ->
    {[
        {<<"application/json">>, handle_get_request}
     ], Req, State}.


content_types_accepted(Req, State) ->
    {[
        {<<"application/json">>, handle_post_request}
     ], Req, State}.


handle_get_request(Req, #state{op=Op} = State) ->
    {ok, Key} = application:get_env(tracker1, jwt_secret),
    case AuthHeader = cowboy_req:header(<<"authorization">>, Req) of
        undefined ->  Resp = cowboy_req:reply(401,#{<<"content-type">> => <<"text/plain">>},<<"Unauthorized">>, Req), {ok, Resp, State};
        _ -> 
            [_Bearer, Token] = string:lexemes(AuthHeader, " "),
            case jwt:decode(Token, Key) of
                {ok, _Claims} -> 
                    {Body_resp, Req_resp, State_resp} = case Op of
                        help ->
                            get_help_request(Req, State);
                        users -> 
                            get_user_request(Req, State);
                        _ -> Resp = cowboy_req:reply(404,#{<<"content-type">> => <<"text/plain">>},<<"Not Found">>, Req), {ok, Resp, State}
                    end,
                    {Body_resp, Req_resp, State_resp};
                {error, _} -> Resp = cowboy_req:reply(401,#{<<"content-type">> => <<"text/plain">>},<<"Unauthorized">>, Req), {ok, Resp, State}
            end
    end.

handle_post_request(Req, #state{op=Op} = State) ->
    {ok, Key} = application:get_env(tracker1, jwt_secret),
    case AuthHeader = cowboy_req:header(<<"authorization">>, Req) of
        undefined ->  Resp = cowboy_req:reply(401,#{<<"content-type">> => <<"text/plain">>},<<"Unauthorized">>, Req), {ok, Resp, State};
        _ -> 
            [_Bearer, Token] = string:lexemes(AuthHeader, " "),
            case jwt:decode(Token, Key) of
                {ok, _Claims} ->
                    {Body_resp, Req_resp, State_resp} = case Op of
                        users ->
                            post_user_request(Req, State)
                    end,
                    {Body_resp, Req_resp, State_resp};
                {error, _} -> Resp = cowboy_req:reply(401,#{<<"content-type">> => <<"text/plain">>},<<"Unauthorized">>, Req), {ok, Resp, State}
            end
    end.

append_time_to_json(OriginalJson) ->

    OriginalLen = length(OriginalJson),
    Substr = lists:sublist(OriginalJson,OriginalLen - 1),

    {{Year, Month, Day},{Hour, Minute, Seconds}} = calendar:universal_time(),

    Body = ",\"datetime(UTC)\": \"~4..0B-~2..0B-~2..0BT~2..0B:~2..0B:~2..0BZ\"}",
    Body1 = io_lib:format(Body, [ Year, Month, Day, Hour, Minute, Seconds]),
    Body_resp = Substr ++ Body1,

    Body_resp.

get_help_request(Req, State) ->
    Body = "{
            \"GET /1/help\": \"Resumes currently supported requests\",
            \"GET /1/users?filename=<filename> \": \" Provides a random subset of users possessing at least one chunk of the file named {filename} \",
            \"POST /1/users \": \" Register a new user with at least one chunk of the file named {filename} \"
    }", 
    {Body, Req, State}.

get_user_request(Req, State) ->

    try
        QsMap = cowboy_req:match_qs([{filename,  nonempty}], Req),
        {ok, Recordfilename} = application:get_env(tracker1, users_file_name),
        dets:open_file(records_db, [{file, Recordfilename}, {type, bag}]),
        {ok, Filename} = maps:find(filename, QsMap),
        Items = dets:lookup(records_db, Filename),
        dets:close(records_db),  
        AllUsers = lists:map(fun ({_, V}) -> V end, Items),
        PickedUsers = pick([10, AllUsers]),
        Body = "
            {
            \"peers\": [~s]
            }",
        BodyResp = io_lib:format(Body, [string:join([[X] || X <- PickedUsers], ",")]),
        {BodyResp, Req, State}
    catch
        _:_ -> Resp = cowboy_req:reply(400,#{<<"content-type">> => <<"text/plain">>},<<"">>, Req), {ok, Resp, State}
    end.

post_user_request(Req, State) ->
    {ok, Json, Req1} = cowboy_req:read_body(Req),
    JsonList = binary_to_list(Json),
    DataToSave = append_time_to_json(JsonList),
    Req_resp = cowboy_req:reply(200, #{<<"content-type">> => <<"application/json">>}, DataToSave, Req1),
    {ok, Recordfilename} = application:get_env(tracker1, users_file_name),
    JsonDecoded =  jsx:decode(Json , [{return_maps, false}]),
    Pid = proplists:get_value(<<"pid">>, JsonDecoded),
    Address = proplists:get_value(<<"address">>, JsonDecoded),
    Port = proplists:get_value(<<"port">>, JsonDecoded),
    Filename = proplists:get_value(<<"filename">>, JsonDecoded),
    BodyToSave = io_lib:format("{\"pid\": \"~s\", \"address\": \"~s\",\"port\": \"~s\"}", [Pid, Address, Port]),
    {ok, _} = dets:open_file(records_db, [{file, Recordfilename}, {type, bag}]),
    ok = dets:insert(records_db, {Filename, BodyToSave}),
    ok = dets:sync(records_db),
    ok = dets:close(records_db),

    {true, Req_resp, State}.
