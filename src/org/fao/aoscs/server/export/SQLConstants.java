package org.fao.aoscs.server.export;

public class SQLConstants {

    
    public static String SetupHeader(){
        return  "" +
            "-- \n"  +
            "-- SQL format exported from the AGROVOC Concept Server Workbench\n" +
            "-- -------------------------------------------------------------\n" +
            "\n"+
            "/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;\n" +
            "/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;\n" +
            "/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;\n" +
            "/*!40101 SET NAMES utf8 */;\n" +
            "\n"+
            "/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;\n" +
            "/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;\n" +
            "/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;\n" +
            "\n"+
            "\n"+
            "--\n" +
            "-- Create schema agrovoc\n" +
            "--\n" +
            "\n"+
            " DROP DATABASE IF EXISTS agrovocexport;\n" +
            " CREATE DATABASE IF NOT EXISTS agrovocexport DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;\n" +
            " USE agrovocexport;\n\n" ;
    }
    
    public static String GenerateCreateTableScript(){
        return ""+
        "--\n"+
        "-- Definition of table `agrovocterm`\n"+
        "--\n"+
        "\n"+
        "DROP TABLE IF EXISTS `agrovocterm`;\n"+
        "CREATE TABLE `agrovocterm` (\n"+
        "  `termcode` varchar(200) NOT NULL default '0',\n"+
        "  `languagecode` varchar(2) NOT NULL default '',\n"+
        "  `termspell` varchar(170) default NULL,\n"+
        "  `statusid` tinyint(3) unsigned default NULL,\n"+
        "  `createdate` date default NULL,\n"+
        "  `frequencyiad` int(11) default NULL,\n"+
        "  `frequencycad` int(11) default NULL,\n"+
        "  `lastupdate` datetime default NULL,\n"+
        "  `scopeid` varchar(2) default NULL,\n"+
        "  `idowner` tinyint(2) default '10',\n"+
        "  `termsense` tinyint(2) default NULL,\n"+
        "  `termoffset` varchar(8) default NULL,\n"+
        "  PRIMARY KEY  (`termcode`,`languagecode`),\n"+
        "  KEY `termspell` (`termspell`),\n"+
        "  KEY `statusid` (`statusid`)\n"+
        ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='InnoDB free: 70656 kB';\n"+
        "\n"+
        "\n"+
        "--\n"+
        "-- Definition of table `categories`\n"+
        "--\n"+
        "\n"+
        "DROP TABLE IF EXISTS `categories`;\n"+
        "CREATE TABLE `categories` (\n"+
        "  `schemeid` varchar(200) NOT NULL default '',\n"+
        "  `categoryid` varchar(200) NOT NULL default '',\n"+
        "  `languagecode` varchar(2) NOT NULL default '',\n"+
        "  `categoryname` varchar(255) default NULL,\n"+
        "  `parentcategoryid` varchar(10) default NULL,\n"+
        "  PRIMARY KEY  (`categoryid`,`languagecode`,`schemeid`),\n"+
        "  KEY `categoryname` (`categoryname`)\n"+
        ") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n"+
        "\n"+
        "\n"+       
        "--\n"+
        "-- Definition of table `catschemes`\n"+
        "--\n"+
        "\n"+
        "DROP TABLE IF EXISTS `catschemes`;\n"+
        "CREATE TABLE `catschemes` (\n"+
        "  `schemeid` varchar(200) NOT NULL default '0',\n"+
        "  `maintenancegroupid` int(11) NOT NULL default '0',\n"+
        "  `scheme` varchar(150) NOT NULL default '',\n"+
        "  `languagecode` varchar(2) NOT NULL default '',\n"+
        "  `active` varchar(1) default NULL\n"+
        "--  PRIMARY KEY  (`schemeid`,`languagecode`),\n"+
        "--  KEY `category` (`schemeid`)\n"+
        ") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n"+
        "\n"+
        "\n"+
        "--\n"+
        "-- Definition of table `language`\n"+
        "--\n"+
        "\n"+
        "DROP TABLE IF EXISTS `language`;\n"+
        "CREATE TABLE `language` (\n"+
        "  `languagecode` varchar(2) NOT NULL default '',\n"+
        "  `name` varchar(15) default NULL,\n"+
        "  `lnggroupid` tinyint(3) unsigned default NULL,\n"+
        "  `originalname` varchar(15) default NULL,\n"+
        "  `lngorder` tinyint(4) default '0',\n"+
        "  `createdate` datetime default NULL,\n"+
        "  PRIMARY KEY  (`languagecode`)\n"+
        ") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n"+
        "\n"+
        "\n"+
        "--\n"+
        "-- Definition of table `linktype`\n"+
        "--\n"+
        "\n"+
        "DROP TABLE IF EXISTS `linktype`;\n"+
        "CREATE TABLE `linktype` (\n"+
        "  `linktypeid` varchar(200) NOT NULL default '',\n"+
        "  `languagecode` varchar(2) NOT NULL default '',\n"+
        "  `linkdesc` varchar(60) default NULL,\n"+
        "  `linkabr` varchar(60) default NULL,\n"+
        "  `linkdescription` longtext,\n"+
        "  `createdate` datetime default NULL,\n"+
        "  `rlinkcode` int(11) unsigned default NULL,\n"+
        "  `parentlinktypeid` int(11) default NULL,\n"+
        "  `linklevel` varchar(2) default NULL,\n"+
        "  PRIMARY KEY  (`linktypeid`,`languagecode`),\n"+
        "  UNIQUE KEY `linkabr` (`linkabr`,`languagecode`)\n"+
        ") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n"+
        "\n"+
        "\n"+
        "--\n"+
        "-- Definition of table `maintenancegroups`\n"+
        "--\n"+
        "\n"+
        "DROP TABLE IF EXISTS `maintenancegroups`;\n"+
        "CREATE TABLE `maintenancegroups` (\n"+
        "  `name` varchar(150) NOT NULL default '',\n"+
        "  `maintenancegroupid` int(11) NOT NULL default '0',\n"+
        "  `login` varchar(30) NOT NULL default '',\n"+
        "  `password` varchar(30) NOT NULL default '',\n"+
        "  PRIMARY KEY  (`name`,`maintenancegroupid`,`login`) \n "+
        ") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n"+
        "\n"+
        "\n"+
        "--\n"+
        "-- Definition of table `mapping`\n"+
        "--\n"+
        "\n"+
        "DROP TABLE IF EXISTS `mapping`;\n"+
        "CREATE TABLE `mapping` (\n"+
        "  `catcode` varchar(5) NOT NULL default '0',\n"+
        "  `termcode` int(11) NOT NULL default '0',\n"+
        "  `schemeid` int(11) NOT NULL default '0',\n"+
        "  `parentcategoryid` varchar(10) NOT NULL default '',\n"+
        "  PRIMARY KEY  (`catcode`,`termcode`,`schemeid`,`parentcategoryid`)\n"+
        ") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n"+
        "\n"+
        "\n"+
        "--\n"+
        "-- Definition of table `scope`\n"+
        "--\n"+
        "\n"+
        "DROP TABLE IF EXISTS `scope`;\n"+
        "CREATE TABLE `scope` (\n"+
        "  `scopeid` varchar(2) NOT NULL default '',\n"+
        "  `scopedesc` varchar(60) default NULL,\n"+
        "  `languagecode` varchar(2) NOT NULL default '',\n"+
        "  `scopegrpid` tinyint(3) unsigned default NULL,\n"+
        "  PRIMARY KEY  (`scopeid`,`languagecode`)\n"+
        ") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n"+
        "\n"+
        "\n"+
        "--\n"+
        "-- Definition of table `tagtype`\n"+
        "--\n"+
        "\n"+
        "DROP TABLE IF EXISTS `tagtype`;\n"+
        "CREATE TABLE `tagtype` (\n"+
        "  `tagtypeid` tinyint(3) unsigned NOT NULL default '0',\n"+
        "  `tagdesc` varchar(60) default NULL,\n"+
        "  `languagecode` varchar(2) NOT NULL default '',\n"+
        "  PRIMARY KEY  (`tagtypeid`,`languagecode`)\n"+
        ") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n"+
        "\n"+
        "\n"+
        "--\n"+
        "-- Definition of table `termlink`\n"+
        "--\n"+
        "\n"+
        "DROP TABLE IF EXISTS `termlink`;\n"+
        "CREATE TABLE `termlink` (\n"+
        "  `termcode1` varchar(200) NOT NULL default '0',\n"+
        "  `termcode2` varchar(200) NOT NULL default '0',\n"+
        "  `languagecode1` varchar(2) default NULL,\n"+
        "  `languagecode2` varchar(2) default NULL,\n"+
        "  `linktypeid` int(11) unsigned NOT NULL default '0',\n"+
        "  `createdate` datetime default NULL,\n"+
        "  `maintenancegroupid` int(11) NOT NULL default '0',\n"+
        "  `newlinktypeid` int(11) default NULL,\n"+
        "  `confirm` varchar(1) default NULL,\n"+
        "  `technique` varchar(5) default NULL,\n"+
        "  `update` datetime default NULL,\n"+
        "  `updmaintenancegroupid` int(11) default NULL,\n"+
        "  PRIMARY KEY  (`termcode1`,`termcode2`,`linktypeid`)\n"+
        ") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n"+
        "\n"+
        "\n"+
        "--\n"+
        "-- Definition of table `termstatus`\n"+
        "--\n"+
        "\n"+
        "DROP TABLE IF EXISTS `termstatus`;\n"+
        "CREATE TABLE `termstatus` (\n"+
        "  `statusid` tinyint(3) unsigned NOT NULL default '0',\n"+
        "  `statusdesc` varchar(60) default NULL,\n"+
        "  `languagecode` varchar(2) NOT NULL default '',\n"+
        "  PRIMARY KEY  (`statusid`,`languagecode`)\n"+
        ") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n"+
        "\n"+
        "\n"+
        "--\n"+
        "-- Definition of table `termtag`\n"+
        "--\n"+
        "\n"+
        "DROP TABLE IF EXISTS `termtag`;\n"+
        "CREATE TABLE `termtag` (\n"+
        //TODO changed from int(11) to varchar(200) 
        "  `termcode` varchar(200) NOT NULL default '0',\n"+ 
        "  `languagecode` varchar(2) NOT NULL default '',\n"+
        "  `tagtypeid` tinyint(3) unsigned NOT NULL default '0',\n"+
        "  `tagtext` text,\n"+
        "  `createdate` datetime default NULL,\n"+
        "  `lastupdate` datetime default NULL,\n"+
        "  PRIMARY KEY  (`tagtypeid`,`languagecode`,`termcode`)\n"+
        ") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n\n";
    }
    
    public static String SetupFinalString(){
        return 
        "/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;\n" +
        "/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;\n" +
        "/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;\n" +
        "/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;\n" +
        "/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;\n" +
        "/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;\n" +
        "/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;\n";
    }
    
    public static String dumpLinkTypeConstants(){
        return 
        "(5, 'AR', 'مرجع الملاحظات الموجزة.', 'SNR', 'X <scope note reference> Y. The scope notes for the term X contains information on the term Y. E.g.: \\'foods\\' <scope_note_reference> \\'feeds\\';', '1998-04-29 16:19:00', 10, NULL, 'TR'),\n" + 
        "(5, 'CS', 'Scope Note Reference', 'SNR', 'X <scope note reference> Y. The scope notes for the term X contains information on the term Y. E.g.: \\'foods\\' <scope_note_reference> \\'feeds\\';', '1998-04-29 16:19:00', 10, NULL, 'TR'),\n" + 
        "(5, 'EN', 'Scope Note Reference', 'SNR', 'X <scope_note_reference> Y. The scope notes for the term X contains information on the term Y. E.g.: \\'foods\\' <scope_note_reference> \\'feeds\\';', '1998-04-29 16:19:00', 10, NULL, 'TR'),\n" + 
        "(5, 'ES', 'Referencia de Nota de Alcance', 'SNR', NULL, '1998-04-29 16:19:00', 10, NULL, 'TR'),\n" + 
        "(5, 'FR', 'Référence de la note d’application', 'SNR', 'X <scope note reference> Y. The scope notes for the term X contains information on the term Y. E.g.: \\'foods\\' <scope_note_reference> \\'feeds\\';', '1998-04-29 16:19:00', 10, NULL, 'TR'),\n" + 
        "(5, 'PT', 'Scope Note Reference', 'SNR', 'X <scope note reference> Y. The scope notes for the term X contains information on the term Y. E.g.: \\'foods\\' <scope_note_reference> \\'feeds\\';', '1998-04-29 16:19:00', 10, NULL, 'TR'),\n" + 
        "(5, 'ZH', 'Scope Note Reference', 'SNR', 'X <scope note reference> Y. The scope notes for the term X contains information on the term Y. E.g.: \\'foods\\' <scope_note_reference> \\'feeds\\';', '1998-04-29 16:19:00', 10, NULL, 'TR'),\n" + 
        "(10, 'AR', 'تم وضعها في مرجع مجال الملاحظات الموجزة. ', 'SNX', 'Y <is referenced in scope note> X. A term Y is contained in the scope explanatory notes for the term X. E.g.: \\'feeds\\' <is_referenced_in_scope note> \\'foods\\';', '1998-04-29 16:19:00', 5, NULL, 'TR'),\n" + 
        "(10, 'CS', 'Is Referenced in Scope Note', 'SNX', 'Y <is referenced in scope note> X. A term Y is contained in the scope explanatory notes for the term X. E.g.: \\'feeds\\' <is_referenced_in_scope note> \\'foods\\';', '1998-04-29 16:19:00', 5, NULL, 'TR'),\n" + 
        "(10, 'EN', 'Is Referenced in Scope Note', 'SNX', 'Y <is_referenced_in_scope_note> X. A term Y is contained in the scope explanatory notes for the term X. E.g.: \\'feeds\\' <is_referenced_in_scope_note> \\'foods\\';', '1998-04-29 16:19:00', 5, NULL, 'TR'),\n" + 
        "(10, 'ES', 'Se refiere a nota de alcance', 'SNX', NULL, '1998-04-29 16:19:00', 5, NULL, 'TR'),\n" + 
        "(10, 'FR', 'Référencé dans la note d’application', 'SNX', 'Y <is referenced in scope note> X. A term Y is contained in the scope explanatory notes for the term X. E.g.: \\'feeds\\' <is_referenced_in_scope note> \\'foods\\';', '1998-04-29 16:19:00', 5, NULL, 'TR'),\n" + 
        "(10, 'PT', 'Is Referenced in Scope Note', 'SNX', 'Y <is referenced in scope note> X. A term Y is contained in the scope explanatory notes for the term X. E.g.: \\'feeds\\' <is_referenced_in_scope note> \\'foods\\';', '1998-04-29 16:19:00', 5, NULL, 'TR'),\n" + 
        "(10, 'ZH', 'Is Referenced in Scope Note', 'SNX', 'Y <is referenced in scope note> X. A term Y is contained in the scope explanatory notes for the term X. E.g.: \\'feeds\\' <is_referenced_in_scope note> \\'foods\\';', '1998-04-29 16:19:00', 5, NULL, 'TR'),\n" + 
        "(20, 'AR', 'المستخدمة لأجل ', 'UF', 'X <used_for> Y. A preferred term X should be used instead of a non-preferred term Y. In some cases, one of a pair of terms substituting the non-preferred term Y. E.g. \\'foods\\' <used_for> \\'food products\\';', '1998-04-29 16:19:00', 70, 106, 'TR'),\n" + 
        "(20, 'CS', 'Užij místo', 'UF', NULL, '2004-05-25 00:00:00', 70, 106, 'TR'),\n" + 
        "(20, 'EN', 'Used For', 'UF', 'X <used_for> Y. A preferred term X should be used instead of a non-preferred term Y. In some cases, one of a pair of terms substituting the non-preferred term Y. E.g. \\'foods\\' <used_for> \\'food products\\';', '1998-04-29 16:19:00', 70, 106, 'TR'),\n" + 
        "(20, 'ES', 'Usado para', 'UF', NULL, '1998-04-29 16:19:00', 70, 106, 'TR'),\n" + 
        "(20, 'FR', 'Employé pour', 'UF', NULL, '2004-05-25 00:00:00', 70, 106, 'TR'),\n" + 
        "(20, 'PT', 'Usado por', 'UF', NULL, '2004-05-25 00:00:00', 70, 106, 'TR'),\n" + 
        "(20, 'ZH', 'Used For', 'UF', 'X <used_for> Y. A preferred term X should be used instead of a non-preferred term Y. In some cases, one of a pair of terms substituting the non-preferred term Y. E.g. \\'foods\\' <used_for> \\'food products\\';', '1998-04-29 16:19:00', 70, 106, 'TR'),\n" + 
        "(30, 'AR', 'المستخدمة لاجل +', 'UF+', 'X <used_for+> Y. X is used in combination with another term Z instead of the non-preferred term Y. E.g. \\'foods\\' <used for+> \\'food conservation\\' (the other term to use is \\'preservation\\');', '1998-04-29 16:19:00', 70, 106, 'TR'),\n" + 
        "(30, 'CS', 'Užij místo plus', 'UF+', NULL, '2004-05-25 00:00:00', 70, 106, 'TR'),\n" + 
        "(30, 'EN', 'Used For+', 'UF+', 'X <used_for+> Y. X is used in combination with another term Z instead of the non-preferred term Y. E.g. \\'foods\\' <used for+> \\'food conservation\\' (the other term to use is \\'preservation\\');', '1998-04-29 16:19:00', 70, 106, 'TR'),\n" + 
        "(30, 'ES', 'Usado para +', 'UF+', NULL, '1998-04-29 16:19:00', 70, 106, 'TR'),\n" + 
        "(30, 'FR', 'Employé dans une combinasion', 'UF+', NULL, '2004-05-25 00:00:00', 70, 106, 'TR'),\n" + 
        "(30, 'PT', 'Usado em combinaçao', 'UF+', NULL, '2004-05-25 00:00:00', 70, 106, 'TR'),\n" + 
        "(30, 'ZH', 'Used For+', 'UF+', 'X <used_for+> Y. X is used in combination with another term Z instead of the non-preferred term Y. E.g. \\'foods\\' <used for+> \\'food conservation\\' (the other term to use is \\'preservation\\');', '1998-04-29 16:19:00', 70, 106, 'TR'),\n" + 
        "(40, 'AR', 'Seen For', 'SF', NULL, '1998-04-29 16:19:00', 80, NULL, 'TR'),\n" + 
        "(40, 'CS', 'Seen For', 'SF', NULL, '1998-04-29 16:19:00', 80, NULL, 'TR'),\n" + 
        "(40, 'EN', 'Seen For', 'SF', NULL, '1998-04-29 16:19:00', 80, NULL, 'TR'),\n" + 
        "(40, 'ES', 'Visto Para', 'SF', NULL, '1998-04-29 16:19:00', 80, NULL, 'TR'),\n" + 
        "(40, 'FR', 'Seen For', 'SF', NULL, '1998-04-29 16:19:00', 80, NULL, 'TR'),\n" + 
        "(40, 'PT', 'Seen For', 'SF', NULL, '1998-04-29 16:19:00', 80, NULL, 'TR'),\n" + 
        "(40, 'ZH', 'Seen For', 'SF', NULL, '1998-04-29 16:19:00', 80, NULL, 'TR'),\n" + 
        "(50, 'AR', 'مصطلحات شاملة', 'BT', 'X <broader_term> Y. Y is a general accepted term (Y\\'(can be used as subject headings). One level up from the main term. E.g. \\'soups\\' <broader_term> \\'foods\\';', '1998-04-29 16:19:00', 60, NULL, 'TH'),\n" + 
        "(50, 'CS', 'Nadrazený termín', 'BT', NULL, '2004-05-25 00:00:00', 60, NULL, 'TH'),\n" + 
        "(50, 'EN', 'Broader Term', 'BT', 'X <broader_term> Y. Y is a general term (Y can be used as subject headings). One level up from the main term. E.g. \\'soups\\' <broader_term> \\'foods\\';', '1998-04-29 16:19:00', 60, NULL, 'TH'),\n" + 
        "(50, 'ES', 'Término genérico', 'BT', NULL, '1998-04-29 16:19:00', 60, NULL, 'TH'),\n" + 
        "(50, 'FR', 'Terme générique', 'BT', NULL, '2004-05-25 00:00:00', 60, NULL, 'TH'),\n" + 
        "(50, 'PT', 'Termo genérico', 'BT', NULL, '2004-05-25 00:00:00', 60, NULL, 'TH'),\n" + 
        "(50, 'ZH', 'Broader Term', 'BT', 'X <broader_term> Y. Y is a general accepted term (Y can be used as subject headings). One level up from the main term. E.g. \\'soups\\' <broader_term> \\'foods\\';', '1998-04-29 16:19:00', 60, NULL, 'TH'),\n" + 
        "(60, 'AR', 'مصطلحات محدودة ', 'NT', 'Y <narrower_term> X . X is a more specific accepted term. One level down from the main term. E.g. \\'foods\\' <narrower_term> \\'soups\\';', '1998-04-29 16:19:00', 50, NULL, 'TH'),\n" + 
        "(60, 'CS', 'Podrazený termín', 'NT', NULL, '2004-05-25 00:00:00', 50, NULL, 'TH'),\n" + 
        "(60, 'EN', 'Narrower Term', 'NT', 'Y <narrower_term> X . X is a more specific term. One level down from the main term. E.g. \\'foods\\' <narrower_term> \\'soups\\';', '1998-04-29 16:19:00', 50, NULL, 'TH'),\n" + 
        "(60, 'ES', 'Término específico', 'NT', NULL, '1998-04-29 16:19:00', 50, NULL, 'TH'),\n" + 
        "(60, 'FR', 'Terme spécifique', 'NT', NULL, '2004-05-25 00:00:00', 50, NULL, 'TH'),\n" + 
        "(60, 'PT', 'Termo específico', 'NT', NULL, '2004-05-25 00:00:00', 50, NULL, 'TH'),\n" + 
        "(60, 'ZH', 'Narrower Term', 'NT', 'Y <narrower_term> X . X is a more specific accepted term. One level down from the main term. E.g. \\'foods\\' <narrower_term> \\'soups\\';', '1998-04-29 16:19:00', 50, NULL, 'TH'),\n" + 
        "(70, 'AR', 'استخدام ', 'USE', 'Y <use> X. This relationship refer to the link between the descriptor (or preferred) X and the non-descriptor (or non-preferred) Y. Use this relationship only for this purpose. E.g. \\'food products\\' <use> \\'foods\\'.', '1998-04-29 16:19:00', 20, 106, 'TR'),\n" + 
        "(70, 'CS', 'Užij', 'USE', NULL, '2004-05-25 00:00:00', 20, 106, 'TR'),\n" + 
        "(70, 'EN', 'Use', 'USE', 'Y <use> X. This relationship refer to the link between the descriptor (or preferred) X and the non-descriptor (or non-preferred) Y. Use this relationship only for this purpose. E.g. \\'food products\\' <use> \\'foods\\'.', '1998-04-29 16:19:00', 20, 106, 'TR'),\n" + 
        "(70, 'ES', 'Usar', 'USE', NULL, '1998-04-29 16:19:00', 20, 106, 'TR'),\n" + 
        "(70, 'FR', 'Employer', 'USE', NULL, '2004-05-25 00:00:00', 20, 106, 'TR'),\n" + 
        "(70, 'PT', 'Use', 'USE', NULL, '2004-05-25 00:00:00', 20, 106, 'TR'),\n" + 
        "(70, 'ZH', 'Use', 'USE', 'Y <use> X. This relationship refer to the link between the descriptor (or preferred) X and the non-descriptor (or non-preferred) Y. Use this relationship only for this purpose. E.g. \\'food products\\' <use> \\'foods\\'.', '1998-04-29 16:19:00', 20, 106, 'TR'),\n" + 
        "(80, 'AR', 'See', 'SEE', NULL, '1998-04-29 16:19:00', 40, NULL, 'TR'),\n" + 
        "(80, 'CS', 'See', 'SEE', NULL, '1998-04-29 16:19:00', 40, NULL, 'TR'),\n" + 
        "(80, 'EN', 'See', 'SEE', NULL, '1998-04-29 16:19:00', 40, NULL, 'TR'),\n" + 
        "(80, 'ES', 'Ver', 'SEE', NULL, '1998-04-29 16:19:00', 40, NULL, 'TR'),\n" + 
        "(80, 'FR', 'See', 'SEE', NULL, '1998-04-29 16:19:00', 40, NULL, 'TR'),\n" + 
        "(80, 'PT', 'See', 'SEE', NULL, '1998-04-29 16:19:00', 40, NULL, 'TR'),\n" + 
        "(80, 'ZH', 'See', 'SEE', NULL, '1998-04-29 16:19:00', 40, NULL, 'TR'),\n" + 
        "(90, 'AR', 'مصطلحات ذات صلة', 'RT', 'X <relatedTerm> Y. Use this whenever is not possible to define a more specific relationship.', '1998-04-29 16:19:00', 90, NULL, 'TR'),\n" + 
        "(90, 'CS', 'Asociovaný termín', 'RT', NULL, '2004-05-25 00:00:00', 90, NULL, 'TR'),\n" + 
        "(90, 'EN', 'Related Term', 'RT', 'X <relatedTerm> Y. Used for non-hierarchical relationships.', '1998-04-29 16:19:00', 90, NULL, 'TR'),\n" + 
        "(90, 'ES', 'Término relativo', 'RT', NULL, '1998-04-29 16:19:00', 90, NULL, 'TR'),\n" + 
        "(90, 'FR', 'Terme associé', 'RT', NULL, '2004-05-25 00:00:00', 90, NULL, 'TR'),\n" + 
        "(90, 'PT', 'Termo relacionado', 'RT', NULL, '2004-05-25 00:00:00', 90, NULL, 'TR'),\n" + 
        "(90, 'ZH', 'Related Term', 'RT', 'X <related_term> Y. Use this whenever is not possible to define a more specific relationship.', '1998-04-29 16:19:00', 90, NULL, 'TR')\n";
    }
}
