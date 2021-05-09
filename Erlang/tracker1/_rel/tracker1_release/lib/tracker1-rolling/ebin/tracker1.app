{application, 'tracker1', [
	{description, ""},
	{vsn, "rolling"},
	{modules, ['base64url','jsx','jsx_config','jsx_consult','jsx_decoder','jsx_encoder','jsx_parser','jsx_to_json','jsx_to_term','jsx_verify','jwk','jwk_tests','jwt','jwt_ecdsa','jwt_tests','tracker1_app','tracker1_handler','tracker1_sup']},
	{registered, [tracker1_sup]},
	{applications, [kernel,stdlib,cowboy]},
	{mod, {tracker1_app, []}},
	{env, []}
]}.