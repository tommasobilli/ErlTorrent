{application, 'tracker3', [
	{description, ""},
	{vsn, "rolling"},
	{modules, ['base64url','jsx','jsx_config','jsx_consult','jsx_decoder','jsx_encoder','jsx_parser','jsx_to_json','jsx_to_term','jsx_verify','jwk','jwk_tests','jwt','jwt_ecdsa','jwt_tests','tracker3_app','tracker3_handler','tracker3_sup']},
	{registered, [tracker3_sup]},
	{applications, [kernel,stdlib,cowboy]},
	{mod, {tracker3_app, []}},
	{env, []}
]}.