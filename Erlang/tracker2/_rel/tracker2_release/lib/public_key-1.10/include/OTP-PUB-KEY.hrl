%% Generated by the Erlang ASN.1 compiler. Version: 5.0.15
%% Purpose: Erlang record definitions for each named and unnamed
%% SEQUENCE and SET, and macro definitions for each value
%% definition in module OTP-PUB-KEY.

-ifndef(_OTP_PUB_KEY_HRL_).
-define(_OTP_PUB_KEY_HRL_, true).

-record('AlgorithmIdentifier-PKCS1', {
  algorithm,
  parameters = asn1_NOVALUE
}).

-record('AttributePKCS-7', {
  type,
  values
}).

-record('AlgorithmIdentifierPKCS-7', {
  algorithm,
  parameters = asn1_NOVALUE
}).

-record('AlgorithmIdentifierPKCS-10', {
  algorithm,
  parameters = asn1_NOVALUE
}).

-record('AttributePKCS-10', {
  type,
  values
}).

-record('SubjectPublicKeyInfo-PKCS-10', {
  algorithm,
  subjectPublicKey
}).

-record('ECPrivateKey', {
  version,
  privateKey,
  parameters = asn1_NOVALUE,
  publicKey = asn1_NOVALUE
}).

-record('DSAPrivateKey', {
  version,
  p,
  q,
  g,
  y,
  x
}).

-record('DHParameter', {
  prime,
  base,
  privateValueLength = asn1_NOVALUE
}).

-record('DigestAlgorithm', {
  algorithm,
  parameters = asn1_NOVALUE
}).

-record('DigestInfoPKCS-1', {
  digestAlgorithm,
  digest
}).

-record('RSASSA-AlgorithmIdentifier', {
  algorithm,
  parameters = asn1_NOVALUE
}).

-record('RSASSA-PSS-params', {
  hashAlgorithm = asn1_DEFAULT,
  maskGenAlgorithm = asn1_DEFAULT,
  saltLength = asn1_DEFAULT,
  trailerField = asn1_DEFAULT
}).

-record('RSAES-AlgorithmIdentifier', {
  algorithm,
  parameters = asn1_NOVALUE
}).

-record('RSAES-OAEP-params', {
  hashAlgorithm = asn1_DEFAULT,
  maskGenAlgorithm = asn1_DEFAULT,
  pSourceAlgorithm = asn1_DEFAULT
}).

-record('OtherPrimeInfo', {
  prime,
  exponent,
  coefficient
}).

-record('RSAPrivateKey', {
  version,
  modulus,
  publicExponent,
  privateExponent,
  prime1,
  prime2,
  exponent1,
  exponent2,
  coefficient,
  otherPrimeInfos = asn1_NOVALUE
}).

-record('RSAPublicKey', {
  modulus,
  publicExponent
}).

-record('PSourceAlgorithm', {
  algorithm,
  parameters = asn1_NOVALUE
}).

-record('MaskGenAlgorithm', {
  algorithm,
  parameters = asn1_NOVALUE
}).

-record('HashAlgorithm', {
  algorithm,
  parameters = asn1_NOVALUE
}).

-record('Curve', {
  a,
  b,
  seed = asn1_NOVALUE
}).

-record('ECParameters', {
  version,
  fieldID,
  curve,
  base,
  order,
  cofactor = asn1_NOVALUE
}).

-record('Pentanomial', {
  k1,
  k2,
  k3
}).

-record('Characteristic-two', {
  m,
  basis,
  parameters
}).

-record('ECDSA-Sig-Value', {
  r,
  s
}).

-record('FieldID', {
  fieldType,
  parameters
}).

-record('ValidationParms', {
  seed,
  pgenCounter
}).

-record('DomainParameters', {
  p,
  g,
  q,
  j = asn1_NOVALUE,
  validationParms = asn1_NOVALUE
}).

-record('Dss-Sig-Value', {
  r,
  s
}).

-record('Dss-Parms', {
  p,
  q,
  g
}).

-record('ACClearAttrs', {
  acIssuer,
  acSerial,
  attrs
}).

-record('AAControls', {
  pathLenConstraint = asn1_NOVALUE,
  permittedAttrs = asn1_NOVALUE,
  excludedAttrs = asn1_NOVALUE,
  permitUnSpecified = asn1_DEFAULT
}).

-record('SecurityCategory', {
  type,
  value
}).

-record('Clearance', {
  policyId,
  classList = asn1_DEFAULT,
  securityCategories = asn1_NOVALUE
}).

-record('RoleSyntax', {
  roleAuthority = asn1_NOVALUE,
  roleName
}).

-record('SvceAuthInfo', {
  service,
  ident,
  authInfo = asn1_NOVALUE
}).

-record('IetfAttrSyntax', {
  policyAuthority = asn1_NOVALUE,
  values
}).

-record('TargetCert', {
  targetCertificate,
  targetName = asn1_NOVALUE,
  certDigestInfo = asn1_NOVALUE
}).

-record('AttCertValidityPeriod', {
  notBeforeTime,
  notAfterTime
}).

-record('IssuerSerial', {
  issuer,
  serial,
  issuerUID = asn1_NOVALUE
}).

-record('V2Form', {
  issuerName = asn1_NOVALUE,
  baseCertificateID = asn1_NOVALUE,
  objectDigestInfo = asn1_NOVALUE
}).

-record('ObjectDigestInfo', {
  digestedObjectType,
  otherObjectTypeID = asn1_NOVALUE,
  digestAlgorithm,
  objectDigest
}).

-record('Holder', {
  baseCertificateID = asn1_NOVALUE,
  entityName = asn1_NOVALUE,
  objectDigestInfo = asn1_NOVALUE
}).

-record('AttributeCertificateInfo', {
  version,
  holder,
  issuer,
  signature,
  serialNumber,
  attrCertValidityPeriod,
  attributes,
  issuerUniqueID = asn1_NOVALUE,
  extensions = asn1_NOVALUE
}).

-record('AttributeCertificate', {
  acinfo,
  signatureAlgorithm,
  signatureValue
}).

-record('IssuingDistributionPoint', {
  distributionPoint = asn1_NOVALUE,
  onlyContainsUserCerts = asn1_DEFAULT,
  onlyContainsCACerts = asn1_DEFAULT,
  onlySomeReasons = asn1_NOVALUE,
  indirectCRL = asn1_DEFAULT,
  onlyContainsAttributeCerts = asn1_DEFAULT
}).

-record('AccessDescription', {
  accessMethod,
  accessLocation
}).

-record('DistributionPoint', {
  distributionPoint = asn1_NOVALUE,
  reasons = asn1_NOVALUE,
  cRLIssuer = asn1_NOVALUE
}).

-record('PolicyConstraints', {
  requireExplicitPolicy = asn1_NOVALUE,
  inhibitPolicyMapping = asn1_NOVALUE
}).

-record('GeneralSubtree', {
  base,
  minimum = asn1_DEFAULT,
  maximum = asn1_NOVALUE
}).

-record('NameConstraints', {
  permittedSubtrees = asn1_NOVALUE,
  excludedSubtrees = asn1_NOVALUE
}).

-record('BasicConstraints', {
  cA = asn1_DEFAULT,
  pathLenConstraint = asn1_NOVALUE
}).

-record('EDIPartyName', {
  nameAssigner = asn1_NOVALUE,
  partyName
}).

-record('AnotherName', {
  'type-id',
  value
}).

-record('PolicyMappings_SEQOF', {
  issuerDomainPolicy,
  subjectDomainPolicy
}).

-record('NoticeReference', {
  organization,
  noticeNumbers
}).

-record('UserNotice', {
  noticeRef = asn1_NOVALUE,
  explicitText = asn1_NOVALUE
}).

-record('PolicyQualifierInfo', {
  policyQualifierId,
  qualifier
}).

-record('PolicyInformation', {
  policyIdentifier,
  policyQualifiers = asn1_NOVALUE
}).

-record('PrivateKeyUsagePeriod', {
  notBefore = asn1_NOVALUE,
  notAfter = asn1_NOVALUE
}).

-record('AuthorityKeyIdentifier', {
  keyIdentifier = asn1_NOVALUE,
  authorityCertIssuer = asn1_NOVALUE,
  authorityCertSerialNumber = asn1_NOVALUE
}).

-record('EncryptedData', {
  version,
  encryptedContentInfo
}).

-record('DigestedData', {
  version,
  digestAlgorithm,
  contentInfo,
  digest
}).

-record('SignedAndEnvelopedData', {
  version,
  recipientInfos,
  digestAlgorithms,
  encryptedContentInfo,
  certificates = asn1_NOVALUE,
  crls = asn1_NOVALUE,
  signerInfos
}).

-record('RecipientInfo', {
  version,
  issuerAndSerialNumber,
  keyEncryptionAlgorithm,
  encryptedKey
}).

-record('EncryptedContentInfo', {
  contentType,
  contentEncryptionAlgorithm,
  encryptedContent = asn1_NOVALUE
}).

-record('EnvelopedData', {
  version,
  recipientInfos,
  encryptedContentInfo
}).

-record('DigestInfoPKCS-7', {
  digestAlgorithm,
  digest
}).

-record('SignerInfo', {
  version,
  issuerAndSerialNumber,
  digestAlgorithm,
  authenticatedAttributes = asn1_NOVALUE,
  digestEncryptionAlgorithm,
  encryptedDigest,
  unauthenticatedAttributes = asn1_NOVALUE
}).

-record('SignerInfo_unauthenticatedAttributes_uaSet_SETOF', {
  type,
  values
}).

-record('SignerInfo_unauthenticatedAttributes_uaSequence_SEQOF', {
  type,
  values
}).

-record('SignedData', {
  version,
  digestAlgorithms,
  contentInfo,
  certificates = asn1_NOVALUE,
  crls = asn1_NOVALUE,
  signerInfos
}).

-record('ContentInfo', {
  contentType,
  content = asn1_NOVALUE
}).

-record('KeyEncryptionAlgorithmIdentifier', {
  algorithm,
  parameters = asn1_NOVALUE
}).

-record('IssuerAndSerialNumber', {
  issuer,
  serialNumber
}).

-record('DigestEncryptionAlgorithmIdentifier', {
  algorithm,
  parameters = asn1_NOVALUE
}).

-record('DigestAlgorithmIdentifier', {
  algorithm,
  parameters = asn1_NOVALUE
}).

-record('ContentEncryptionAlgorithmIdentifier', {
  algorithm,
  parameters = asn1_NOVALUE
}).

-record('SignerInfoAuthenticatedAttributes_aaSet_SETOF', {
  type,
  values
}).

-record('SignerInfoAuthenticatedAttributes_aaSequence_SEQOF', {
  type,
  values
}).

-record('CertificationRequest', {
  certificationRequestInfo,
  signatureAlgorithm,
  signature
}).

-record('CertificationRequest_signatureAlgorithm', {
  algorithm,
  parameters = asn1_NOVALUE
}).

-record('CertificationRequestInfo', {
  version,
  subject,
  subjectPKInfo,
  attributes
}).

-record('CertificationRequestInfo_subjectPKInfo', {
  algorithm,
  subjectPublicKey
}).

-record('CertificationRequestInfo_subjectPKInfo_algorithm', {
  algorithm,
  parameters = asn1_NOVALUE
}).

-record('CertificationRequestInfo_attributes_SETOF', {
  type,
  values
}).

-record('PreferredSignatureAlgorithm', {
  sigIdentifier,
  certIdentifier = asn1_NOVALUE
}).

-record('CrlID', {
  crlUrl = asn1_NOVALUE,
  crlNum = asn1_NOVALUE,
  crlTime = asn1_NOVALUE
}).

-record('ServiceLocator', {
  issuer,
  locator
}).

-record('RevokedInfo', {
  revocationTime,
  revocationReason = asn1_NOVALUE
}).

-record('SingleResponse', {
  certID,
  certStatus,
  thisUpdate,
  nextUpdate = asn1_NOVALUE,
  singleExtensions = asn1_NOVALUE
}).

-record('ResponseData', {
  version = asn1_DEFAULT,
  responderID,
  producedAt,
  responses,
  responseExtensions = asn1_NOVALUE
}).

-record('BasicOCSPResponse', {
  tbsResponseData,
  signatureAlgorithm,
  signature,
  certs = asn1_NOVALUE
}).

-record('ResponseBytes', {
  responseType,
  response
}).

-record('OCSPResponse', {
  responseStatus,
  responseBytes = asn1_NOVALUE
}).

-record('CertID', {
  hashAlgorithm,
  issuerNameHash,
  issuerKeyHash,
  serialNumber
}).

-record('Request', {
  reqCert,
  singleRequestExtensions = asn1_NOVALUE
}).

-record('Signature', {
  signatureAlgorithm,
  signature,
  certs = asn1_NOVALUE
}).

-record('TBSRequest', {
  version = asn1_DEFAULT,
  requestorName = asn1_NOVALUE,
  requestList,
  requestExtensions = asn1_NOVALUE
}).

-record('OCSPRequest', {
  tbsRequest,
  optionalSignature = asn1_NOVALUE
}).

-record('TeletexDomainDefinedAttribute', {
  type,
  value
}).

-record('PresentationAddress', {
  pSelector = asn1_NOVALUE,
  sSelector = asn1_NOVALUE,
  tSelector = asn1_NOVALUE,
  nAddresses
}).

-record('ExtendedNetworkAddress_e163-4-address', {
  number,
  'sub-address' = asn1_NOVALUE
}).

-record('PDSParameter', {
  'printable-string' = asn1_NOVALUE,
  'teletex-string' = asn1_NOVALUE
}).

-record('UnformattedPostalAddress', {
  'printable-address' = asn1_NOVALUE,
  'teletex-string' = asn1_NOVALUE
}).

-record('TeletexPersonalName', {
  surname,
  'given-name' = asn1_NOVALUE,
  initials = asn1_NOVALUE,
  'generation-qualifier' = asn1_NOVALUE
}).

-record('ExtensionAttribute', {
  'extension-attribute-type',
  'extension-attribute-value'
}).

-record('BuiltInDomainDefinedAttribute', {
  type,
  value
}).

-record('PersonalName', {
  surname,
  'given-name' = asn1_NOVALUE,
  initials = asn1_NOVALUE,
  'generation-qualifier' = asn1_NOVALUE
}).

-record('BuiltInStandardAttributes', {
  'country-name' = asn1_NOVALUE,
  'administration-domain-name' = asn1_NOVALUE,
  'network-address' = asn1_NOVALUE,
  'terminal-identifier' = asn1_NOVALUE,
  'private-domain-name' = asn1_NOVALUE,
  'organization-name' = asn1_NOVALUE,
  'numeric-user-identifier' = asn1_NOVALUE,
  'personal-name' = asn1_NOVALUE,
  'organizational-unit-names' = asn1_NOVALUE
}).

-record('ORAddress', {
  'built-in-standard-attributes',
  'built-in-domain-defined-attributes' = asn1_NOVALUE,
  'extension-attributes' = asn1_NOVALUE
}).

-record('AlgorithmIdentifier', {
  algorithm,
  parameters = asn1_NOVALUE
}).

-record('TBSCertList', {
  version = asn1_NOVALUE,
  signature,
  issuer,
  thisUpdate,
  nextUpdate = asn1_NOVALUE,
  revokedCertificates = asn1_NOVALUE,
  crlExtensions = asn1_NOVALUE
}).

-record('TBSCertList_revokedCertificates_SEQOF', {
  userCertificate,
  revocationDate,
  crlEntryExtensions = asn1_NOVALUE
}).

-record('CertificateList', {
  tbsCertList,
  signatureAlgorithm,
  signature
}).

-record('Extension', {
  extnID,
  critical = asn1_DEFAULT,
  extnValue
}).

-record('SubjectPublicKeyInfo', {
  algorithm,
  subjectPublicKey
}).

-record('Validity', {
  notBefore,
  notAfter
}).

-record('TBSCertificate', {
  version = asn1_DEFAULT,
  serialNumber,
  signature,
  issuer,
  validity,
  subject,
  subjectPublicKeyInfo,
  issuerUniqueID = asn1_NOVALUE,
  subjectUniqueID = asn1_NOVALUE,
  extensions = asn1_NOVALUE
}).

-record('Certificate', {
  tbsCertificate,
  signatureAlgorithm,
  signature
}).

-record('AttributeTypeAndValue', {
  type,
  value
}).

-record('Attribute', {
  type,
  values
}).

-record('Extension-Any', {
  extnID,
  critical = asn1_DEFAULT,
  extnValue
}).

-record('OTPExtension', {
  extnID,
  critical = asn1_DEFAULT,
  extnValue
}).

-record('OTPExtensionAttribute', {
  extensionAttributeType,
  extensionAttributeValue
}).

-record('OTPCharacteristic-two', {
  m,
  basis,
  parameters
}).

-record('OTPFieldID', {
  fieldType,
  parameters
}).

-record('PublicKeyAlgorithm', {
  algorithm,
  parameters = asn1_NOVALUE
}).

-record('SignatureAlgorithm-Any', {
  algorithm,
  parameters = asn1_NOVALUE
}).

-record('SignatureAlgorithm', {
  algorithm,
  parameters = asn1_NOVALUE
}).

-record('OTPSubjectPublicKeyInfo-Any', {
  algorithm,
  subjectPublicKey
}).

-record('OTPSubjectPublicKeyInfo', {
  algorithm,
  subjectPublicKey
}).

-record('OTPOLDSubjectPublicKeyInfo', {
  algorithm,
  subjectPublicKey
}).

-record('OTPOLDSubjectPublicKeyInfo_algorithm', {
  algo,
  parameters = asn1_NOVALUE
}).

-record('OTPAttributeTypeAndValue', {
  type,
  value
}).

-record('OTPTBSCertificate', {
  version = asn1_DEFAULT,
  serialNumber,
  signature,
  issuer,
  validity,
  subject,
  subjectPublicKeyInfo,
  issuerUniqueID = asn1_NOVALUE,
  subjectUniqueID = asn1_NOVALUE,
  extensions = asn1_NOVALUE
}).

-record('OTPCertificate', {
  tbsCertificate,
  signatureAlgorithm,
  signature
}).

-define('dhKeyAgreement', {1,2,840,113549,1,3,1}).
-define('pkcs-3', {1,2,840,113549,1,3}).
-define('rSASSA-PSS-Default-Identifier', {'RSASSA-AlgorithmIdentifier',{1,2,840,113549,1,1,10},{'RSASSA-PSS-params',{'HashAlgorithm',{1,3,14,3,2,26},'NULL'},{'MaskGenAlgorithm',{1,2,840,113549,1,1,8},{'HashAlgorithm',{1,3,14,3,2,26},'NULL'}},20,1}}).
-define('rSAES-OAEP-Default-Identifier', {'RSAES-AlgorithmIdentifier',{1,2,840,113549,1,1,7},{'RSAES-OAEP-params',{'HashAlgorithm',{1,3,14,3,2,26},'NULL'},{'MaskGenAlgorithm',{1,2,840,113549,1,1,8},{'HashAlgorithm',{1,3,14,3,2,26},'NULL'}},{'PSourceAlgorithm',{1,2,840,113549,1,1,9},<<>>}}}).
-define('emptyString', <<>>).
-define('pSpecifiedEmpty', {'PSourceAlgorithm',{1,2,840,113549,1,1,9},<<>>}).
-define('mgf1SHA1', {'MaskGenAlgorithm',{1,2,840,113549,1,1,8},{'HashAlgorithm',{1,3,14,3,2,26},'NULL'}}).
-define('sha1', {'HashAlgorithm',{1,3,14,3,2,26},'NULL'}).
-define('id-mgf1', {1,2,840,113549,1,1,8}).
-define('id-sha512', {2,16,840,1,101,3,4,2,3}).
-define('id-sha384', {2,16,840,1,101,3,4,2,2}).
-define('id-sha256', {2,16,840,1,101,3,4,2,1}).
-define('id-sha224', {2,16,840,1,101,3,4,2,4}).
-define('id-hmacWithSHA512', {1,2,840,113549,2,11}).
-define('id-hmacWithSHA384', {1,2,840,113549,2,10}).
-define('id-hmacWithSHA256', {1,2,840,113549,2,9}).
-define('id-hmacWithSHA224', {1,2,840,113549,2,8}).
-define('id-md5', {1,2,840,113549,2,5}).
-define('id-md2', {1,2,840,113549,2,2}).
-define('id-sha1', {1,3,14,3,2,26}).
-define('sha-1WithRSAEncryption', {1,3,14,3,2,29}).
-define('sha512-256WithRSAEncryption', {1,2,840,113549,1,1,16}).
-define('sha512-224WithRSAEncryption', {1,2,840,113549,1,1,15}).
-define('sha512WithRSAEncryption', {1,2,840,113549,1,1,13}).
-define('sha384WithRSAEncryption', {1,2,840,113549,1,1,12}).
-define('sha256WithRSAEncryption', {1,2,840,113549,1,1,11}).
-define('sha224WithRSAEncryption', {1,2,840,113549,1,1,14}).
-define('sha1WithRSAEncryption', {1,2,840,113549,1,1,5}).
-define('md5WithRSAEncryption', {1,2,840,113549,1,1,4}).
-define('md2WithRSAEncryption', {1,2,840,113549,1,1,2}).
-define('id-RSASSA-PSS', {1,2,840,113549,1,1,10}).
-define('id-pSpecified', {1,2,840,113549,1,1,9}).
-define('id-RSAES-OAEP', {1,2,840,113549,1,1,7}).
-define('rsaEncryption', {1,2,840,113549,1,1,1}).
-define('pkcs-1', {1,2,840,113549,1,1}).
-define('id-Ed448', {1,3,101,113}).
-define('id-Ed25519', {1,3,101,112}).
-define('id-X448', {1,3,101,111}).
-define('id-X25519', {1,3,101,110}).
-define('id-edwards-curve-algs', {1,3,101}).
-define('sect571r1', {1,3,132,0,39}).
-define('sect571k1', {1,3,132,0,38}).
-define('sect409r1', {1,3,132,0,37}).
-define('sect409k1', {1,3,132,0,36}).
-define('secp521r1', {1,3,132,0,35}).
-define('secp384r1', {1,3,132,0,34}).
-define('secp224r1', {1,3,132,0,33}).
-define('secp224k1', {1,3,132,0,32}).
-define('secp192k1', {1,3,132,0,31}).
-define('secp160r2', {1,3,132,0,30}).
-define('secp128r2', {1,3,132,0,29}).
-define('secp128r1', {1,3,132,0,28}).
-define('sect233r1', {1,3,132,0,27}).
-define('sect233k1', {1,3,132,0,26}).
-define('sect193r2', {1,3,132,0,25}).
-define('sect193r1', {1,3,132,0,24}).
-define('sect131r2', {1,3,132,0,23}).
-define('sect131r1', {1,3,132,0,22}).
-define('sect283r1', {1,3,132,0,17}).
-define('sect283k1', {1,3,132,0,16}).
-define('sect163r2', {1,3,132,0,15}).
-define('secp256k1', {1,3,132,0,10}).
-define('secp160k1', {1,3,132,0,9}).
-define('secp160r1', {1,3,132,0,8}).
-define('secp112r2', {1,3,132,0,7}).
-define('secp112r1', {1,3,132,0,6}).
-define('sect113r2', {1,3,132,0,5}).
-define('sect113r1', {1,3,132,0,4}).
-define('sect239k1', {1,3,132,0,3}).
-define('sect163r1', {1,3,132,0,2}).
-define('sect163k1', {1,3,132,0,1}).
-define('secp256r1', {1,2,840,10045,3,1,7}).
-define('secp192r1', {1,2,840,10045,3,1,1}).
-define('ellipticCurve', {1,3,132,0}).
-define('certicom-arc', {1,3,132}).
-define('id-ecPublicKey', {1,2,840,10045,2,1}).
-define('id-publicKeyType', {1,2,840,10045,2}).
-define('ppBasis', {1,2,840,10045,1,2,3,3}).
-define('tpBasis', {1,2,840,10045,1,2,3,2}).
-define('gnBasis', {1,2,840,10045,1,2,3,1}).
-define('id-characteristic-two-basis', {1,2,840,10045,1,2,3}).
-define('characteristic-two-field', {1,2,840,10045,1,2}).
-define('prime-field', {1,2,840,10045,1,1}).
-define('id-fieldType', {1,2,840,10045,1}).
-define('ecdsa-with-SHA512', {1,2,840,10045,4,3,4}).
-define('ecdsa-with-SHA384', {1,2,840,10045,4,3,3}).
-define('ecdsa-with-SHA256', {1,2,840,10045,4,3,2}).
-define('ecdsa-with-SHA224', {1,2,840,10045,4,3,1}).
-define('ecdsa-with-SHA2', {1,2,840,10045,4,3}).
-define('ecdsa-with-SHA1', {1,2,840,10045,4,1}).
-define('id-ecSigType', {1,2,840,10045,4}).
-define('ansi-X9-62', {1,2,840,10045}).
-define('id-keyExchangeAlgorithm', {2,16,840,1,101,2,1,1,22}).
-define('dhpublicnumber', {1,2,840,10046,2,1}).
-define('id-dsaWithSHA1', {1,3,14,3,2,27}).
-define('id-dsa-with-sha1', {1,2,840,10040,4,3}).
-define('id-dsa', {1,2,840,10040,4,1}).
-define('id-at-clearance', {2,5,1,5,55}).
-define('id-at-role', {2,5,4,72}).
-define('id-aca-encAttrs', {1,3,6,1,5,5,7,10,6}).
-define('id-aca-group', {1,3,6,1,5,5,7,10,4}).
-define('id-aca-chargingIdentity', {1,3,6,1,5,5,7,10,3}).
-define('id-aca-accessIdentity', {1,3,6,1,5,5,7,10,2}).
-define('id-aca-authenticationInfo', {1,3,6,1,5,5,7,10,1}).
-define('id-aca', {1,3,6,1,5,5,7,10}).
-define('id-ce-targetInformation', {2,5,29,55}).
-define('id-pe-ac-proxying', {1,3,6,1,5,5,7,1,10}).
-define('id-pe-aaControls', {1,3,6,1,5,5,7,1,6}).
-define('id-pe-ac-auditIdentity', {1,3,6,1,5,5,7,1,4}).
-define('id-ce-invalidityDate', {2,5,29,24}).
-define('id-holdinstruction-reject', {2,2,840,10040,2,3}).
-define('id-holdinstruction-callissuer', {2,2,840,10040,2,2}).
-define('id-holdinstruction-none', {2,2,840,10040,2,1}).
-define('holdInstruction', {2,2,840,10040,2}).
-define('id-ce-holdInstructionCode', {2,5,29,23}).
-define('id-ce-certificateIssuer', {2,5,29,29}).
-define('id-ce-cRLReasons', {2,5,29,21}).
-define('id-ce-deltaCRLIndicator', {2,5,29,27}).
-define('id-ce-issuingDistributionPoint', {2,5,29,28}).
-define('id-ce-cRLNumber', {2,5,29,20}).
-define('id-pe-subjectInfoAccess', {1,3,6,1,5,5,7,1,11}).
-define('id-pe-authorityInfoAccess', {1,3,6,1,5,5,7,1,1}).
-define('id-ce-freshestCRL', {2,5,29,46}).
-define('id-ce-inhibitAnyPolicy', {2,5,29,54}).
-define('id-kp-OCSPSigning', {1,3,6,1,5,5,7,3,9}).
-define('id-kp-timeStamping', {1,3,6,1,5,5,7,3,8}).
-define('id-kp-emailProtection', {1,3,6,1,5,5,7,3,4}).
-define('id-kp-codeSigning', {1,3,6,1,5,5,7,3,3}).
-define('id-kp-clientAuth', {1,3,6,1,5,5,7,3,2}).
-define('id-kp-serverAuth', {1,3,6,1,5,5,7,3,1}).
-define('anyExtendedKeyUsage', {2,5,29,37,0}).
-define('id-ce-extKeyUsage', {2,5,29,37}).
-define('id-ce-cRLDistributionPoints', {2,5,29,31}).
-define('id-ce-policyConstraints', {2,5,29,36}).
-define('id-ce-nameConstraints', {2,5,29,30}).
-define('id-ce-basicConstraints', {2,5,29,19}).
-define('id-ce-subjectDirectoryAttributes', {2,5,29,9}).
-define('id-ce-issuerAltName', {2,5,29,18}).
-define('id-ce-subjectAltName', {2,5,29,17}).
-define('id-ce-policyMappings', {2,5,29,33}).
-define('anyPolicy', {2,5,29,32,0}).
-define('id-ce-certificatePolicies', {2,5,29,32}).
-define('id-ce-privateKeyUsagePeriod', {2,5,29,16}).
-define('id-ce-keyUsage', {2,5,29,15}).
-define('id-ce-subjectKeyIdentifier', {2,5,29,14}).
-define('id-ce-authorityKeyIdentifier', {2,5,29,35}).
-define('id-ce', {2,5,29}).
-define('id-extensionReq', {2,16,840,1,113733,1,9,8}).
-define('id-transId', {2,16,840,1,113733,1,9,7}).
-define('id-recipientNonce', {2,16,840,1,113733,1,9,6}).
-define('id-senderNonce', {2,16,840,1,113733,1,9,5}).
-define('id-failInfo', {2,16,840,1,113733,1,9,4}).
-define('id-pkiStatus', {2,16,840,1,113733,1,9,3}).
-define('id-messageType', {2,16,840,1,113733,1,9,2}).
-define('id-attributes', {2,16,840,1,113733,1,9}).
-define('id-pki', {2,16,840,1,113733,1}).
-define('id-VeriSign', {2,16,840,1,113733}).
-define('encryptedData', {1,2,840,113549,1,7,6}).
-define('digestedData', {1,2,840,113549,1,7,5}).
-define('signedAndEnvelopedData', {1,2,840,113549,1,7,4}).
-define('envelopedData', {1,2,840,113549,1,7,3}).
-define('signedData', {1,2,840,113549,1,7,2}).
-define('data', {1,2,840,113549,1,7,1}).
-define('pkcs-7', {1,2,840,113549,1,7}).
-define('pkcs-9-at-counterSignature', {1,2,840,113549,1,9,6}).
-define('pkcs-9-at-signingTime', {1,2,840,113549,1,9,5}).
-define('pkcs-9-at-messageDigest', {1,2,840,113549,1,9,4}).
-define('pkcs-9-at-contentType', {1,2,840,113549,1,9,3}).
-define('pkcs-9', {1,2,840,113549,1,9}).
-define('pkcs-9-at-extensionRequest', {1,2,840,113549,1,9,14}).
-define('pkcs-9-at-challengePassword', {1,2,840,113549,1,9,7}).
-define('brainpoolP512t1', {1,3,36,3,3,2,8,1,1,14}).
-define('brainpoolP512r1', {1,3,36,3,3,2,8,1,1,13}).
-define('brainpoolP384t1', {1,3,36,3,3,2,8,1,1,12}).
-define('brainpoolP384r1', {1,3,36,3,3,2,8,1,1,11}).
-define('brainpoolP320t1', {1,3,36,3,3,2,8,1,1,10}).
-define('brainpoolP320r1', {1,3,36,3,3,2,8,1,1,9}).
-define('brainpoolP256t1', {1,3,36,3,3,2,8,1,1,8}).
-define('brainpoolP256r1', {1,3,36,3,3,2,8,1,1,7}).
-define('brainpoolP224t1', {1,3,36,3,3,2,8,1,1,6}).
-define('brainpoolP224r1', {1,3,36,3,3,2,8,1,1,5}).
-define('brainpoolP192t1', {1,3,36,3,3,2,8,1,1,4}).
-define('brainpoolP192r1', {1,3,36,3,3,2,8,1,1,3}).
-define('brainpoolP160t1', {1,3,36,3,3,2,8,1,1,2}).
-define('brainpoolP160r1', {1,3,36,3,3,2,8,1,1,1}).
-define('versionOne', {1,3,36,3,3,2,8,1,1}).
-define('ellipticCurveRFC5639', {1,3,36,3,3,2,8,1}).
-define('ecStdCurvesAndGeneration', {1,3,36,3,3,2,8}).
-define('id-aes256-wrap', {2,16,840,1,101,3,4,1,45}).
-define('id-aes192-wrap', {2,16,840,1,101,3,4,1,25}).
-define('id-aes128-wrap', {2,16,840,1,101,3,4,1,5}).
-define('id-aes256-CBC', {2,16,840,1,101,3,4,1,42}).
-define('id-aes192-CBC', {2,16,840,1,101,3,4,1,22}).
-define('id-aes128-CBC', {2,16,840,1,101,3,4,1,2}).
-define('aes', {2,16,840,1,101,3,4,1}).
-define('id-pkix-ocsp-extended-revoke', {1,3,6,1,5,5,7,48,1,9}).
-define('id-pkix-ocsp-pref-sig-algs', {1,3,6,1,5,5,7,48,1,8}).
-define('id-pkix-ocsp-service-locator', {1,3,6,1,5,5,7,48,1,7}).
-define('id-pkix-ocsp-archive-cutoff', {1,3,6,1,5,5,7,48,1,6}).
-define('id-pkix-ocsp-nocheck', {1,3,6,1,5,5,7,48,1,5}).
-define('id-pkix-ocsp-response', {1,3,6,1,5,5,7,48,1,4}).
-define('id-pkix-ocsp-crl', {1,3,6,1,5,5,7,48,1,3}).
-define('id-pkix-ocsp-nonce', {1,3,6,1,5,5,7,48,1,2}).
-define('id-pkix-ocsp-basic', {1,3,6,1,5,5,7,48,1,1}).
-define('id-pkix-ocsp', {1,3,6,1,5,5,7,48,1}).
-define('ub-x121-address-length', 16).
-define('ub-unformatted-address-length', 180).
-define('ub-terminal-id-length', 24).
-define('ub-surname-length', 40).
-define('ub-pseudonym-universal', 256).
-define('ub-pseudonym-utf8', 256).
-define('ub-pseudonym', 128).
-define('ub-postal-code-length', 16).
-define('ub-pds-physical-address-lines', 6).
-define('ub-pds-parameter-length', 30).
-define('ub-pds-name-length', 16).
-define('ub-organizational-units', 4).
-define('ub-numeric-user-id-length', 32).
-define('ub-integer-options', 256).
-define('ub-initials-length', 5).
-define('ub-given-name-length', 16).
-define('ub-generation-qualifier-length', 3).
-define('ub-e163-4-sub-address-length', 40).
-define('ub-e163-4-number-length', 15).
-define('ub-extension-attributes', 256).
-define('ub-domain-name-length', 16).
-define('ub-domain-defined-attribute-value-length', 128).
-define('ub-domain-defined-attribute-type-length', 8).
-define('ub-domain-defined-attributes', 4).
-define('ub-country-name-numeric-length', 3).
-define('ub-country-name-alpha-length', 2).
-define('ub-emailaddress-length', 255).
-define('ub-match', 128).
-define('ub-serial-number', 64).
-define('ub-title-utf8', 256).
-define('ub-title-universal', 256).
-define('ub-title-printable', 128).
-define('ub-title-teletex', 128).
-define('ub-title', 64).
-define('ub-organizational-unit-name-utf8', 256).
-define('ub-organizational-unit-name-universal', 256).
-define('ub-organizational-unit-name-teletex', 128).
-define('ub-organizational-unit-name-printable', 128).
-define('ub-organizational-unit-name', 64).
-define('ub-organization-name-utf8', 256).
-define('ub-organization-name-universal', 256).
-define('ub-organization-name-teletex', 128).
-define('ub-organization-name-printable', 128).
-define('ub-organization-name', 64).
-define('ub-state-name-utf8', 256).
-define('ub-state-name-universal', 256).
-define('ub-state-name', 128).
-define('ub-locality-name-universal', 256).
-define('ub-locality-name-utf8', 256).
-define('ub-locality-name', 128).
-define('ub-common-name-utf8', 256).
-define('ub-common-name-universal', 256).
-define('ub-common-name-printable', 128).
-define('ub-common-name-teletex', 128).
-define('ub-common-name', 64).
-define('ub-name-utf8', 131072).
-define('ub-name-universal', 131072).
-define('ub-name-printable', 65536).
-define('ub-name-teletex', 65536).
-define('ub-name', 32768).
-define('teletex-domain-defined-attributes', 6).
-define('terminal-type', 23).
-define('extended-network-address', 22).
-define('local-postal-attributes', 21).
-define('unique-postal-name', 20).
-define('poste-restante-address', 19).
-define('post-office-box-address', 18).
-define('street-address', 17).
-define('unformatted-postal-address', 16).
-define('extension-physical-delivery-address-components', 15).
-define('physical-delivery-organization-name', 14).
-define('physical-delivery-personal-name', 13).
-define('extension-OR-address-components', 12).
-define('physical-delivery-office-number', 11).
-define('physical-delivery-office-name', 10).
-define('postal-code', 9).
-define('physical-delivery-country-name', 8).
-define('pds-name', 7).
-define('teletex-organizational-unit-names', 5).
-define('teletex-personal-name', 4).
-define('teletex-organization-name', 3).
-define('teletex-common-name', 2).
-define('common-name', 1).
-define('id-emailAddress', {1,2,840,113549,1,9,1}).
-define('id-domainComponent', {0,9,2342,19200300,100,1,25}).
-define('id-at-pseudonym', {2,5,4,65}).
-define('id-at-serialNumber', {2,5,4,5}).
-define('id-at-countryName', {2,5,4,6}).
-define('id-at-dnQualifier', {2,5,4,46}).
-define('id-at-title', {2,5,4,12}).
-define('id-at-organizationalUnitName', {2,5,4,11}).
-define('id-at-organizationName', {2,5,4,10}).
-define('id-at-stateOrProvinceName', {2,5,4,8}).
-define('id-at-localityName', {2,5,4,7}).
-define('id-at-commonName', {2,5,4,3}).
-define('id-at-generationQualifier', {2,5,4,44}).
-define('id-at-initials', {2,5,4,43}).
-define('id-at-givenName', {2,5,4,42}).
-define('id-at-surname', {2,5,4,4}).
-define('id-at-name', {2,5,4,41}).
-define('id-at', {2,5,4}).
-define('id-ad-caRepository', {1,3,6,1,5,5,7,48,5}).
-define('id-ad-timeStamping', {1,3,6,1,5,5,7,48,3}).
-define('id-ad-caIssuers', {1,3,6,1,5,5,7,48,2}).
-define('id-ad-ocsp', {1,3,6,1,5,5,7,48,1}).
-define('id-qt-unotice', {1,3,6,1,5,5,7,2,2}).
-define('id-qt-cps', {1,3,6,1,5,5,7,2,1}).
-define('id-ad', {1,3,6,1,5,5,7,48}).
-define('id-kp', {1,3,6,1,5,5,7,3}).
-define('id-qt', {1,3,6,1,5,5,7,2}).
-define('id-pe', {1,3,6,1,5,5,7,1}).
-define('id-pkix', {1,3,6,1,5,5,7}).
-define('id-dsa-with-sha256', {2,16,840,1,101,3,4,3,2}).
-define('id-dsa-with-sha224', {2,16,840,1,101,3,4,3,1}).
-endif. %% _OTP_PUB_KEY_HRL_
