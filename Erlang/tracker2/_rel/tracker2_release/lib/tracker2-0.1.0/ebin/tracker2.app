{application, 'tracker2', [
	{description, "New project"},
	{vsn, "0.1.0"},
	{modules, ['tracker2_app','tracker2_sup']},
	{registered, [tracker2_sup]},
	{applications, [kernel,stdlib]},
	{mod, {tracker2_app, []}},
	{env, []}
]}.