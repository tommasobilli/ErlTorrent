{application, 'tracker3', [
	{description, "New project"},
	{vsn, "0.1.0"},
	{modules, ['tracker3_app','tracker3_sup']},
	{registered, [tracker3_sup]},
	{applications, [kernel,stdlib]},
	{mod, {tracker3_app, []}},
	{env, []}
]}.