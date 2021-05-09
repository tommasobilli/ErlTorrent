{application, 'tracker2', [
	{description, ""},
	{vsn, "rolling"},
	{modules, ['base64url','jsx','jsx_config','jsx_consult','jsx_decoder','jsx_encoder','jsx_parser','jsx_to_json','jsx_to_term','jsx_verify','jwk','jwk_tests','jwt','jwt_ecdsa','jwt_tests','tracker2_app','tracker2_handler','tracker2_sup']},
	{registered, [tracker2_sup]},
	{applications, [kernel,stdlib,cowboy]},
	{mod, {tracker2_app, []}},
	{env, []}
]}.