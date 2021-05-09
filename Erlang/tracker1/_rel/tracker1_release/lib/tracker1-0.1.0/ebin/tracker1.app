{application, 'tracker1', [
	{description, "New project"},
	{vsn, "0.1.0"},
	{modules, ['tracker1_app','tracker1_sup']},
	{registered, [tracker1_sup]},
	{applications, [kernel,stdlib]},
	{mod, {tracker1_app, []}},
	{env, []}
]}.