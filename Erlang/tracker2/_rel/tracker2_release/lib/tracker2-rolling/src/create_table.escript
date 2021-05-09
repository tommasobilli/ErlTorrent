#!/usr/bin/env escript    

main([Filename]) ->
    Filename1 = io_lib:format("~s_users.dets", [Filename]),
    dets:open_file(users_tab, [{file, Filename1}, {type, bag}]),
    dets:close(users_tab);
main(["--help"]) ->
    usage();
main([]) ->
    usage().

usage() ->
    io:fwrite("usage: create_table.escript <db-file-stem>~n", []).