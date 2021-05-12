- mkdir tracker1
- cd tracker1

- wget https://erlang.mk/erlang.mk

- make -f erlang.mk bootstrap bootstrap-rel

- Aprire file sys.config. Cancellare il contenuto e sostituire con:

PROJECT = tracker1

DEPS = cowboy
dep_cowboy_commit = 2.6.3

DEP_PLUGINS = cowboy

include erlang.mk

- Nella shell ( all'interno della directory tracker1): make new t=cowboy.http n=tracker1_handler

- All'interno di "tracker1_handler": cancellare contenuto e incollare il contenuto del file "tracker1_handler.erl" all'interno della directory "Erlang" nella repository
- All'interno di "tracker1_app": cancellare contenuto e incollare il contenuto del file "tracker1_app.erl" all'interno della directory "Erlang" nella repository

- Creare nella directory tracker1 una cartella chiamata "Data"

- Nella shell ( all'interno della directory tracker1): 

./src/create_table.escript Data/tracker1

- Nella direcory "config" in tracker1, eliminare il contenuto del file e incollare (chiaramente il path al file "tracker1_users.dets" è da modificare in base al proprio path"):
	
[
    {tracker1,
        [
            {users_file_name, "/Users/andreadidonato/tracker1/Data/tracker1_users.dets"},
            {jwt_secret, "supas3cri7"}
        ]
    }
].

- Copiare ed incollare all'interno della directory "src" le repository "erlang-base64url-master", "jsx-main", "jwt-master"

- Per avviare: make run 


- Per creare i tracker 2 e 3 il procedimento è lo stesso dell'1. Ovviamente dove vi è la stringa "tracker1" sostituire con la stringa "tracker2" o "tracker3" a seconda del tracker che si sta considerando. Per il tracker2 la porta consigliata (da settare in tracker2_app.erl) è la 8082 mentre per il tracker3 è la 8083.