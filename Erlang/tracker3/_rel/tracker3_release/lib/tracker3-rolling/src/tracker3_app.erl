-module(tracker3_app).
-behaviour(application).

-export([start/2]).
-export([stop/1]).

start(_Type, _Args) ->
    Dispatch = cowboy_router:compile([
        {'_', [
                {"/1/help", tracker3_handler, [help]},
                {"/1/users", tracker3_handler, [users]}
        ]}
    ]),
    {ok, _} = cowboy:start_clear(my_http_listener,
        [{port, 8083}],
        #{env => #{dispatch => Dispatch}}
    ),
    error_logger:tty(false),
    tracker3_sup:start_link().

stop(_State) ->
	ok.