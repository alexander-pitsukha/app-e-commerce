INSERT INTO users
    (id,"createdAt","deletedAt","updatedAt",disabled,email,"emailVerificationToken","emailVerificationTokenExpiresAt","emailVerified","firstName","importHash","lastName",password,"passwordResetToken","passwordResetTokenExpiresAt","phoneNumber",provider,role,"createdById","updatedById")
VALUES
    ('52BC1FB60FE546478CBC8C55E156B889','2020-11-17 14:32:23.553',NULL,'2020-11-17 14:32:23.553',false,'admin@flatlogic.com',NULL,NULL,true,'Admin',NULL,NULL,'$2b$12$Dtk2u7HCtJydGeljMR5tEeKmVxrO8W0FNoSDXSSBoJHrNU3i9R4ca',NULL,NULL,NULL,'local','ADMIN','52BC1FB60FE546478CBC8C55E156B889',NULL),
    ('127B7B845AF04E6693CA346AC35E1BDD','2020-12-10 17:33:50.152',NULL,NULL,false,'user@flatlogic.com',NULL,NULL,false,'Alex',NULL,'Xela',NULL,NULL,NULL,'+888888888888',NULL,'USER','52BC1FB60FE546478CBC8C55E156B889',NULL),
    ('12740C3D27884401AF6CB869EC4A4639','2020-12-11 17:33:50.152',NULL,NULL,false,'user1@flatlogic.com','37F1FE9C2C574A5CB2CB16FE2FC705C4','2020-12-10 00:00:00',false,'Alexa',NULL,'Alexa','$2b$12$Dtk2u7HCtJydGeljMR5tEeKmVxrO8W0FNoSDXSSBoJHrNU3i9R4ca','E04715B3A09B4E0B8D72697B1855EB82',NULL,'+999999999999',NULL,'USER','52BC1FB60FE546478CBC8C55E156B889',NULL);

INSERT INTO categories
    (id,"createdAt","deletedAt","updatedAt","importHash",title,"createdById","updatedById")
VALUES
    ('D160A5FE122443F4A05FCBC60E97F2D5','2020-12-10 17:32:23.846',NULL,NULL,NULL,'cars','52BC1FB60FE546478CBC8C55E156B889',NULL),
    ('1F5FEED0B48740898B8992C09AC4C65D','2020-12-10 17:32:27.908',NULL,NULL,NULL,'clocks','52BC1FB60FE546478CBC8C55E156B889',NULL),
    ('28AEA915029F43EC914B28C8A9217027','2020-12-10 17:32:32.119',NULL,NULL,NULL,'tables','52BC1FB60FE546478CBC8C55E156B889',NULL),
    ('3EA4C02198794315B61E0AAC16936732','2020-12-10 17:32:39.702',NULL,NULL,NULL,'computers','52BC1FB60FE546478CBC8C55E156B889',NULL);

INSERT INTO products
    (id,"createdAt","deletedAt","updatedAt",description,discount,"importHash",price,rating,status,title,"createdById","updatedById")
VALUES
    ('0F5A5C459A7542A5986A4BE6FBF5A3D4','2020-12-10 17:34:23.75',NULL,NULL,'description',10.00,NULL,100.00,2,'IN_STOCK','product1','52BC1FB60FE546478CBC8C55E156B889',NULL),
    ('6B2D31376EDD4201A1C30624DF6F704A','2020-12-10 17:34:54.11',NULL,NULL,'desc',10.00,NULL,100.00,2,'OUT_OF_STOCK','product2','52BC1FB60FE546478CBC8C55E156B889',NULL);

INSERT INTO orders
    (id,"createdAt","deletedAt","updatedAt",amount,"importHash",order_date,status,"createdById","updatedById","productId","userId")
VALUES
    ('8050F33A19034AC7A7D1A6C3A77834BD','2020-12-10 17:35:15.799',NULL,NULL,10,NULL,'2020-12-10 00:00:00','IN_CART','52BC1FB60FE546478CBC8C55E156B889',NULL,'0F5A5C459A7542A5986A4BE6FBF5A3D4','127B7B845AF04E6693CA346AC35E1BDD'),
    ('6D2DD84AF17F4BE88CB1255AA88342C3','2020-12-10 17:35:30.893',NULL,NULL,20,NULL,'2020-12-17 00:00:00','BOUGHT','52BC1FB60FE546478CBC8C55E156B889',NULL,'6B2D31376EDD4201A1C30624DF6F704A','127B7B845AF04E6693CA346AC35E1BDD');

INSERT INTO "productsCategoriesCategories"
    ("productId","categoryId")
VALUES
    ('0F5A5C459A7542A5986A4BE6FBF5A3D4','D160A5FE122443F4A05FCBC60E97F2D5'),
    ('0F5A5C459A7542A5986A4BE6FBF5A3D4','3EA4C02198794315B61E0AAC16936732'),
    ('6B2D31376EDD4201A1C30624DF6F704A','D160A5FE122443F4A05FCBC60E97F2D5');

INSERT INTO "productsMore_productsProducts"
    ("productId","moreProductId")
VALUES
    ('6B2D31376EDD4201A1C30624DF6F704A','0F5A5C459A7542A5986A4BE6FBF5A3D4');

INSERT INTO files
    (id, "createdAt", "deletedAt", "updatedAt", "belongsTo", "belongsToColumn", "belongsToId", name, "privateUrl", "publicUrl", "sizeInBytes", "createdById", "updatedById")
VALUES
    ('94951CE110444B238E4E23AEEDF03787', '2022-12-30 09:25:30.693', NULL, NULL, 'users', 'avatar', '52BC1FB60FE546478CBC8C55E156B889', 'Screenshot_20221229_104032.png', 'users/avatar/58ae5c09-a337-41c0-acf2-07073041247a.png', 'http://localhost:8080/api/file/download?privateUrl=users/avatar/58ae5c09-a337-41c0-acf2-07073041247a.png', 114563, '52BC1FB60FE546478CBC8C55E156B889', NULL),
    ('D05258B0C3D645ED8E75C1CBCBC805A3', '2022-12-30 09:25:30.693', NULL, NULL, 'products', 'avatar', '6B2D31376EDD4201A1C30624DF6F704A', 'Screenshot_20221229_104032.png', 'users/avatar/58ae5c09-a337-41c0-acf2-07073041247a.png', 'http://localhost:8080/api/file/download?privateUrl=users/avatar/58ae5c09-a337-41c0-acf2-07073041247a.png', 114563, '52BC1FB60FE546478CBC8C55E156B889', NULL);
