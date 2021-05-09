-module(tracker2_app).
-behaviour(application).

-export([start/2]).
-export([stop/1]).

start(_Type, _Args) ->
    Dispatch = cowboy_router:compile([
        {'_', [
                {"/1/help", tracker2_handler, [help]},
                {"/1/users", tracker2_handler, [users]}
        ]}
    ]),
    {ok, _} = cowboy:start_clear(my_http_listener,
        [{port, 8082}],
        #{env => #{dispatch => Dispatch}}
    ),
    error_logger:tty(false),
    tracker2_sup:start_link().

stop(_State) ->
	ok.