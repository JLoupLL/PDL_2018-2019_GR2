package be.devijver.wikipedia.parser.javacc.table;

import java.io.ByteArrayInputStream;
import java.io.StringReader;

import junit.framework.TestCase;
import be.devijver.wikipedia.parser.ast.Attribute;
import be.devijver.wikipedia.parser.ast.Cell;
import be.devijver.wikipedia.parser.ast.Row;
import be.devijver.wikipedia.parser.ast.Table;

public class JavaCCTableParserTests extends TestCase {

	private Table parse(String s) {
		try {
			return new JavaCCTableParser(
					new StringReader(s)
				).parseTable();
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public void testEmptyTable() throws Exception {
		Table t = parse("{| |}");

		System.out.println(t);
		assertNotNull(t);

		assertNull(t.getCaption());
		assertNull(t.getTableOptions());
		assertNull(t.getCaptionOptions());
	}

	public void testEmptyTableWithTableOptions() throws Exception {
		Table t = parse("{| border=\"0\" |}");

		System.out.println(t);
		assertNotNull(t);

		assertNull(t.getCaption());
		assertNull(t.getCaptionOptions());
		assertNotNull(t.getTableOptions());

		assertEquals(1, t.getTableOptions().getAttributes().length);

		Attribute attr = t.getTableOptions().getAttributes()[0];

		assertEquals("border", attr.getName());
		assertEquals("0", attr.getValue());
	}

	public void testEmptyTableWithCaption() throws Exception {
		Table t = parse("{|\n|+ This is a caption |}");

		System.out.println(t);
		assertNotNull(t);

		assertEquals(" This is a caption ", t.getCaption());
		assertNull(t.getTableOptions());
	}

	public void testTableWithCaptionAndCaptionOptions1() throws Exception {
		Table t = parse(
			"{| border=\"0\" \n" +
			"|+ someoption=\"true\" | This is a caption \n" +
			"|}");

		System.out.println(t);
		assertNotNull(t);

		assertNotNull(t.getTableOptions());
		assertEquals(" This is a caption ", t.getCaption());
		assertNotNull(t.getCaptionOptions());

		assertEquals(1, t.getCaptionOptions().getAttributes().length);
	}

	public void testTableWithCaptionAndCaptionOptions2() throws Exception {
		Table t = parse(
			"{| border=\"0\" \n" +
			"|+ someoption=\"true\" | '''This is a caption''' \n" +
			"|}");

		System.out.println(t);
		assertNotNull(t);

		assertNotNull(t.getTableOptions());
		assertEquals(" '''This is a caption''' ", t.getCaption());
		assertNotNull(t.getCaptionOptions());

		assertEquals(1, t.getCaptionOptions().getAttributes().length);
	}

	public void testTableWithOneRowAndOneCell() throws Exception {
		Table t = parse(
			"{|\n" +
			"|-\n" +
			"| cell1\n" +
			"|}");

		System.out.println(t);
	}

	public void testTableWithOneRowWithOneCellWithCellOptions() throws Exception {
		Table t = parse(
			"{|\n" +
			"|-\n" +
			"| style=\"none\" | cell1 \n" +
			"|}");

			System.out.println(t);

	}

	public void testTableWithOneRowWithTwoCellsWithCellOptions() throws Exception {
		Table t = parse(
			"{|\n" +
			"|-\n" +
			"| style=\"none\" | cell1 || style=\"groovy\" | cell2 \n" +
			"|}"
		);

		System.out.println(t);

		assertNotNull(t);
		assertNull(t.getCaption());
		assertNull(t.getCaptionOptions());
		assertNull(t.getTableOptions());

		assertEquals(1, t.getRows().length);
		assertEquals(2, t.getRows()[0].getCells().length);
		Cell cell1 = t.getRows()[0].getCells()[0];
		Cell cell2 = t.getRows()[0].getCells()[1];

		assertEquals(" cell1 ", cell1.getContent()[0].toString());
		assertEquals(1, cell1.getOptions().getAttributes().length);
		assertEquals("style", cell1.getOptions().getAttributes()[0].getName());
		assertEquals("none", cell1.getOptions().getAttributes()[0].getValue());

		assertEquals(" cell2 ", cell2.getContent()[0].toString());
		assertEquals(1, cell2.getOptions().getAttributes().length);
		assertEquals("style", cell2.getOptions().getAttributes()[0].getName());
		assertEquals("groovy", cell2.getOptions().getAttributes()[0].getValue());
	}


	public void testTableWithOneRowAndTwoCells1() throws Exception {
		Table t = parse(
				"{|\n" +
				"|-\n" +
				"| cell1 || cell2\n" +
				"|}");

		System.out.println(t);
	}

	public void testTableWithOneRowAndTwoCells2() throws Exception {
		Table t = parse(
				"{|\n" +
				"|-\n" +
				"| cell1 \n" +
				"| cell2 \n" +
				"|}");
		System.out.println(t);
	}

	public void testTableWithOneRowAndTwoCells3() throws Exception {
		Table t = parse(
				"{|\n" +
				"|-\n" +
				"! cell1 !! cell2\n" +
				"|}");

		System.out.println(t);
	}

	public void testTableWithOneRowAndTwoCells4() throws Exception {
		Table t = parse(
				"{|\n" +
				"|-\n" +
				"! cell1 \n" +
				"! cell2\n" +
				"|}");

		System.out.println(t);
		assertNotNull(t);
		assertNull(t.getCaption());
		assertNull(t.getCaptionOptions());
		assertNull(t.getTableOptions());

		assertEquals(1, t.getRows().length);
		assertEquals(2, t.getRows()[0].getCells().length);
		Cell cell1 = t.getRows()[0].getCells()[0];
		Cell cell2 = t.getRows()[0].getCells()[1];

		assertNull(cell1.getOptions());
		assertEquals(" cell1 ", cell1.getContent()[0].toString());

		assertNull(cell1.getOptions());
		assertEquals(" cell2", cell2.getContent()[0].toString());
	}

	public void testTableWithNestedTable1() throws Exception {
		Table t = parse(
			"{|\n" +
			"|-\n" +
			"|  {| border=\"0\" |}\n" +
			"|}"
		);

		System.out.println(t);
	}

	public void testTableWithNestedTable2() throws Exception {
		Table t = parse(
			"{|\n" +
			"|-\n" +
			"|{| border=\"0\" |}\n" +
			"|}"
		);

		System.out.println(t);
	}

	public void testTableWithTwoRows() throws Exception {
		Table t = parse(
			"{|\n" +
			"|-\n" +
			"| row 1, cell 1 || row 1, cell 2\n" +
			"|-\n" +
			"| row 2, cell 1 || row 2, cell 2\n" +
			"|}"
		);

		System.out.println(t);
	}

	public void testWebApplicationFrameworksTable1() throws Exception {
		String content =
			"{| class=\"wikitable sortable\" style=\"font-size: 90%\"\n" +
			"|-\n" +
			"! Project\n" +
			"! Current Stable Version\n" +
			"! [[Programming Language|Language]]\n" +
			"! [[License]]\n" +
			"|-\n" +
			"! {{rh}} | [[Agavi_(framework)|Agavi]]\n" +
			"| 0.11 RC6\n" +
			"| [[PHP]]\n" +
			"| [[LGPL]] \n" +
			"|-\n" +
			"! {{rh}} | [[AJILE|Ajile]]\n" +
			"| [http://prdownloads.sourceforge.net/ajile/Ajile.0.9.9.2.zip?download 0.9.9.2]\n" +
			"| [[JavaScript]]\n" +
			"| [[MPL|MPL 1.1]] / [[LGPL|LGPL 2.1]] / [[GPL|GPL 2.0]] \n" +
			"|-\n" +
			"! {{rh}} | [[Akelos PHP Framework|Akelos]]\n" +
			"| 0.8\n" +
			"| [[PHP]]\n" +
			"| [[LGPL]] \n" +
			"|-\n" +
			"! {{rh}} | [[Apache Struts]]\n" +
			"| 2.0.9\n" +
			"| [[Java (programming language)|Java]]\n" +
			"| [[Apache License|Apache]] \n" +
			"|-\n" +
			"! {{rh}} | [[Andromeda Database|Andromeda]]\n" +
			"| 2007.08.08\n" +
			"| [[PHP]]\n" +
			"| [[GPL]]\n" +
			"|-\n" +
			"! {{rh}} | [[Aranea framework|Aranea MVC]]\n" +
			"| 1.0.10\n" +
			"| [[Java (programming language)|Java]]\n" +
			"| [[Apache License|Apache]]\n" +
			"|-\n" +
			"! {{rh}} | [[CakePHP]]\n" +
			"| 1.1.17.5612\n" +
			"| [[PHP]]\n" +
			"| [[MIT License|MIT]]\n" +
			"|-\n" +
			"! {{rh}} | [[Camping (microframework)|Camping]]\n" +
			"| 1.5\n" +
			"| [[Ruby (programming language)|Ruby]]\n" +
			"| [[MIT License|MIT]]\n" +
			"|-\n" +
			"! {{rh}} | [[Catalyst (software)|Catalyst]]\n" +
			"| 5.7007\n" +
			"| [[Perl]]\n" +
			"| [[GPL]]/[[Artistic License|Artistic]]\n" +
			"|-\n" +
			"! {{rh}} | [[CodeIgniter|Code Igniter]]\n" +
			"| 1.5.4\n" +
			"| [[PHP]]\n" +
			"| [http://codeigniter.com/user_guide/license.html Apache/BSD-style open source license]\n" +
			"|-\n" +
			"! {{rh}} | [[ColdBox]]\n" +
			"| 2.0.3\n" +
			"| [[Adobe ColdFusion|ColdFusion]]\n" +
			"| [[Apache License|Apache]]\n" +
			"|-\n" +
			"! {{rh}} | [[Django (web framework)|Django]]\n" +
			"| 0.96\n" +
			"| [[Python (programming language)|Python]]\n| [[BSD]]\n" +
			"|-\n" +
			"! {{rh}} | [[DotNetNuke]]\n" +
			"| 4.6.2\n" +
			"| [[ASP.NET]]\n" +
			"| [[BSD]]\n" +
			"|-\n" +
			"! {{rh}} | [[Fusebox (programming)|Fusebox]]\n" +
			"| 5.1\n" +
			"| [[Adobe ColdFusion|ColdFusion]]\n" +
			"| [[Apache License|Apache]]\n" +
			"|-\n" +
			"! {{rh}} | [http://mdp.cti.depaul.edu/examples Gluon]\n" +
			"| 1.5\n" +
			"| [[Python (programming language)|Python]]\n" +
			"| [[GPL]]\n" +
			"|-\n" +
			"! {{rh}} | [[Grails_%28Framework%29|Grails]]\n" +
			"| 0.6 \n" +
			"| [[Groovy (programming language)|Groovy]]\n" +
			"| [[Apache License|Apache]]\n" +
			"|-\n" +
			"! {{rh}} | [[JBoss Seam]]\n" +
			"| 1.2.1 GA\n" +
			"| [[Java (programming language)|Java]]\n" +
			"| [[LGPL]]\n" +
			"|-\n" +
			"! {{rh}} | [[Jifty]]\n" +
			"| 0.70824\n" +
			"| [[Perl]]\n" +
			"| [[GPL]]/[[Artistic License|Artistic]]\n" +
			"|-\n" +
			"! {{rh}} | [[Kumbia]]\n" +
			"| 0.46RC9\n" +
			"| [[PHP]]\n" +
			"| [[GPL]] | [[Apache License|Apache 2.0]] | [[PHP5]]\n" +
			"|-\n" +
			"! {{rh}} | [[Lift (web framework)|Lift]]\n" +
			"| 0.2.0\n" +
			"| [[Scala (programming language)|Scala]]\n" +
			"| [[Apache License|Apache 2.0]]\n" +
			"|-\n" +
			"! {{rh}} | [[Mach-II]]\n" +
			"| 1.5\n" +
			"| [[Adobe ColdFusion|ColdFusion]]\n" +
			"| [[Apache License|Apache]]\n" +
			"|-\n" +
			"! {{rh}} | [[Makumba]]\n" +
			"| 0.5.16\n" +
			"| [[Java (programming language)|Java]]\n" +
			"| [[LGPL]]\n" +
			"|-\n" +
			"! {{rh}} | [[Model-Glue]]\n" +
			"| 2.0\n" +
			"| [[Adobe ColdFusion|ColdFusion]]\n" +
			"| [[Apache License|Apache]]\n" +
			"|-\n" +
			"! {{rh}} | [[Monorail (.Net)|MonoRail]]\n" +
			"| 1.0 RC3\n" +
			"| [[ASP.NET]]\n" +
			"| [[Apache License|Apache]]\n" +
			"|-\n" +
			"! {{rh}} | [[Nitro (web framework)|Nitro]]\n" +
			"| 0.41\n" +
			"| [[Ruby (programming language)|Ruby]]\n" +
			"| [[BSD License]]\n" +
			"|-\n" +
			"! {{rh}} | [[OpenACS]]\n" +
			"| 5.3.2\n" +
			"| [[Tcl]]\n" +
			"| [[GPL]]\n" +
			"|-\n" +
			"! {{rh}} | [[PHPulse]]\n" +
			"| 2.0\n" +
			"| [[PHP]]\n" +
			"| [[GPL]]\n" +
			"|-\n" +
			"! {{rh}} | [[PRADO]]\n" +
			"| 3.1.1\n" +
			"| [[PHP]]\n" +
			"| [[BSD License]]\n" +
			"|-\n" +
			"! {{rh}} | [[Pylons (web framework)]]\n" +
			"| 0.9.6\n" +
			"| [[Python (programming language)|Python]]\n" +
			"| [[BSD License]]\n" +
			"|-\n" +
			"! {{rh}} | [[Qcodo]]\n" +
			"| 0.3.32\n" +
			"| [[PHP]]\n" +
			"| [[MIT License]]\n" +
			"|-\n" +
			"! {{rh}} | [[RIFE]]\n" +
			"| 1.6.2\n" +
			"| [[Java]]\n" +
			"| [[Apache License|Apache]]\n" +
			"|-\n" +
			"! {{rh}} | [[Ruby on Rails]]\n" +
			"| 1.2.3\n" +
			"| [[Ruby (programming language)|Ruby]]\n" +
			"| [[MIT License|MIT]]/[[Ruby License|Ruby]]\n" +
			"|-\n" +
			"! {{rh}} | [[Seaside web framework|Seaside]]\n" +
			"| 2.7\n" +
			"| [[Smalltalk]]\n" +
			"| [[MIT License]]\n" +
			"|-\n" +
			"! {{rh}} | [[Spring Framework]]\n" +
			"| 2.0.6\n" +
			"| [[Java (programming language)|Java]]\n" +
			"| [[Apache License|Apache]]\n" +
			"|-\n" +
			"! {{rh}} | [[Stripes]]\n" +
			"| 1.4.3\n" +
			"| [[Java]]\n" +
			"| [[LGPL]]\n" +
			"|-\n" +
			"! {{rh}} | [[Symfony]]\n" +
			"| 1.0.7\n" +
			"| [[PHP]]\n" +
			"| [[MIT License|MIT]]\n" +
			"|-\n" +
			"! {{rh}} | [[TurboGears]]\n" +
			"| 1.0.1\n" +
			"| [[Python (programming language)|Python]]\n" +
			"| [[MIT License]], [[LGPL]]\n" +
			"|-\n" +
			"! {{rh}} | [[WebLeaf]]\n" +
			"| 2.1\n" +
			"| [[Java (programming language)|Java]]\n" +
			"| [[LGPL]]\n" +
			"|-\n" +
			"! {{rh}} | [[WebObjects]]\n" +
			"| 5.3.1\n" +
			"| [[Java (programming language)|Java]]\n" +
			"| [[Proprietary]]\n" +
			"|-\n" +
			"! {{rh}} | [[Zend Framework]]\n" +
			"| [http://framework.zend.com/download 1.0.2]\n" +
			"| [[PHP]]\n" +
			"| [[BSD_Licenses|BSD License]]\n" +
			"|-\n" +
			"! {{rh}} | [[Zoop Framework]]\n" +
			"| 1.2\n" +
			"| [[PHP]]\n" +
			"| [[Zope Public License|ZPL]]\n" +
			"|-\n" +
			"! {{rh}} | [[Zope]]2\n" +
			"| 2.10\n" +
			"| [[Python]]\n" +
			"| [[Zope Public License|ZPL]]\n" +
			"|-\n" +
			"! {{rh}} | [[Zope]]3\n" +
			"| 3.3\n" +
			"| [[Python]]\n" +
			"| [[Zope Public License|ZPL]]\n" +
			"|-class=\"sortbottom\"\n" +
			"! Project\n" +
			"! Current Stable Version\n" +
			"! [[Programming Language|Language]]\n" +
			"! [[License]]\n" +
			"|}";

		Table t = parse(content);

		System.out.println(t);
		Row row = t.getRows()[1];
		Cell cell = row.getCells()[0];

		assertEquals(" [[Agavi_(framework)|Agavi]]", cell.getContent()[0].toString());
		assertEquals("{{rh}}", cell.getOptions().getAttributes()[0].getName());
	}

	public void testWebApplicationFrameworks2() throws Exception {
		String content =
			"{| class=\"wikitable sortable\" style=\"font-size: 90%\"\n" +
			"|-\n" +
			"!Project\n" +
			"!Language\n" +
			"![[Ajax (programming)|Ajax]]\n" +
			"![[Model-view-controller|MVC]] framework\n" +
			"![[Web_application_framework#Push-based_vs._Pull-based|MVC Push/Pull]]\n" +
			"![[Internationalization_and_localization|i18n & l10n?]]\n" +
			"![[Object-relational mapping|ORM]]\n" +
			"!Testing framework(s)\n" +
			"!DB migration framework(s)\n" +
			"!Security Framework(s)\n" +
			"\n" +
			"!Template Framework(s)\n" +
			"!Caching Framework(s)\n" +
			"!Form Validation Framework(s)\n" +
			"|-\n" +
			"|[[Agavi_(framework)|Agavi]]\n" +
			"| PHP\n" +
			"| \n" +
			"| {{Yes}}\n" +
			"| Push\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"|-\n" +
			"|[[AJILE|Ajile]]\n" +
			"| [[JavaScript]]\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"| Push & Pull\n" +
			"| {{Yes}}\n" +
			"| \n" +
			"| {{Yes}}, [http://jsunit.net/ jsUnit]\n" +
			"| \n" +
			"| \n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"| \n" +
			"|-\n" +
			"|[[Akelos PHP Framework]]\n" +
			"| PHP\n" +
			"| {{Yes}}, [[Prototype JavaScript Framework|Prototype]], [[script.aculo.us]]\n" +
			"| {{Yes}}, [[Active record pattern|ActiveRecord]]\n" +
			"| Push\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}, [[Active record pattern|ActiveRecord]]\n" +
			"| {{Yes}}, [[Unit testing|Unit Tests]]\n" +
			"| {{Yes}}\n" +
			"| \n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"|-\n" +
			"|[[Apache Struts]]\n" +
			"| Java\n" +
			"| {{Yes}}\n" +
			"| \n" +
			"| Push\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}, [[Unit testing|Unit Tests]]\n" +
			"|\n" +
			"|\n" +
			"|{{Yes}}, Jakarta Tiles framework\n" +
			"|\n" +
			"|{{Yes}}, Jakarta Validator framework\n" +
			"|-\n" +
			"|[[Apache Struts 2 (ex. WebWork)]]\n" +
			"| Java\n" +
			"| {{Yes}}\n" +
			"| \n" +
			"| Push & Pull\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}, [[Unit testing|Unit Tests]]\n" +
			"|\n" +
			"|\n" +
			"|{{Yes}}\n" +
			"|\n" +
			"|{{Yes}}\n" +
			"|-\n" +
			"|[[Andromeda Database|Andromeda]]\n" +
			"| PHP\n" +
			"|{{Yes}}\n" +
			"|\n" +
			"|\n" +
			"|{{Yes}}\n" +
			"|{{Yes}}\n" +
			"|\n" +
			"|\n" +
			"|{{Yes}}\n" +
			"|{{Yes}}\n" +
			"|\n" +
			"|{{Yes}}\n" +
			"|-\n" +
			"|[[Aranea framework|Aranea MVC]]\n" +
			"| Java\n" +
			"| {{Yes}}\n" +
			"| \n" +
			"| Pull\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"|\n" +
			"|\n" +
			"|\n" +
			"|\n" +
			"|\n" +
			"|\n" +
			"|-\n" +
			"|[[CakePHP]]\n" +
			"| PHP\n" +
			"| {{Yes}}, [[Prototype JavaScript Framework|Prototype]], [[script.aculo.us]]\n" +
			"| [[Active record pattern|ActiveRecord]]\n" +
			"| Push\n" +
			"| {{Yes}}, Development branch\n" +
			"| {{Yes}}, [[Active record pattern|ActiveRecord]]\n" +
			"| {{Yes}}, [[Unit testing|Unit Tests]]\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}, [[Access control list|ACL-based]]\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}, Development branch\n" +
			"| {{Yes}}\n" +
			"|-\n" +
			"|[[Camping (microframework)|Camping]]\n" +
			"| Ruby\n" +
			"| {{No}}\n" +
			"| {{Yes}}\n" +
			"| Push\n" +
			"| {{No}}\n" +
			"| {{Yes}}, [[Active record pattern|ActiveRecord]]\n" +
			"| {{Yes}}, via [http://mosquito.rubyforge.org/ Mosquito]\n" +
			"| {{Yes}}\n" +
			"| {{No}}\n" +
			"| {{Yes}}\n" +
			"| {{No}}\n" +
			"| {{No}}\n" +
			"|-\n" +
			"|[[Catalyst (software)|Catalyst]]\n" +
			"| Perl\n" +
			"| {{Yes}}, multiple ([[Prototype JavaScript Framework|Prototype]], [[Dojo Toolkit|Dojo]]...)\n" +
			"| {{Yes}}\n" +
			"| Push in its most common usage\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}, multiple ([[DBIx::Class]], [[Rose::DB]]...)\n" +
			"| {{Yes}}<ref name=\"catalysttest\">http://search.cpan.org/dist/Catalyst-Manual/lib/Catalyst/Manual/Tutorial/Testing.pod</ref>\n" +
			"|\n" +
			"| {{Yes}}, multiple ([[Access control list|ACL-based]], external engines...)\n" +
			"| {{Yes}}, multiple (Template::Toolkit, HTML::Template, HTML::Mason...)\n" +
			"| {{Yes}}, multiple (Memcached, TurckMM, shared memory,...)\n" +
			"| {{Yes}}, multiple (HTML::FormValidator,...)\n" +
			"|-\n" +
			"|[[CodeIgniter]]\n" +
			"| PHP\n" +
			"| {{No}}\n" +
			"| {{Yes}}, Modified [[Active record pattern|ActiveRecord]]\n" +
			"| Push\n" +
			"| {{Yes}}\n" +
			"| {{No}}\n" +
			"| {{Yes}}, [[Unit testing|Unit Tests]]\n" +
			"| {{No}}\n" +
			"| {{Yes}}\n" +
			"| {{No}}\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"|-\n" +
			"|[[ColdBox]]\n" +
			"| [[ColdFusion]]\n" +
			"| {{Yes}}, via CF or any JavaScript Library\n" +
			"| {{Yes}}\n" +
			"| Push\n" +
			"| {{Yes}} provided by framework\n" +
			"| {{Yes}} [[Transfer]], [[Reactor]], [[Hibernate]], ObjectBreeze\n" +
			"| {{Yes}}, Integrated into Core Framework, [[CFUnit]], [[CFCUnit]]\n" +
			"| {{No}}\n" +
			"| {{Yes}}, via plugin or interceptors\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}, ColdBox Internal Caching Engine, and via [[ColdSpring]]\n" +
			"| {{Yes}}, via cf validation or custom interceptors\n" +
			"|-\n" +
			"|[[Django (web framework)|Django]]\n" +
			"| Python\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"| Push\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}, [[Django ORM]], [[SQLAlchemy]]\n" +
			"| {{Yes}}\n" +
			"| {{No}}\n" +
			"| {{Yes}}, [[Access control list|ACL-based]]\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"|-\n" +
			"|[[DotNetNuke]]\n" +
			"| .NET\n" +
			"| {{Yes}}\n" +
			"| \n" +
			"| Pull\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}, SubSonic, NHibernate\n" +
			"|\n" +
			"|\n" +
			"|\n" +
			"|\n" +
			"|\n" +
			"|\n" +
			"|-\n" +
			"|[[Fusebox (programming)|Fusebox]]\n" +
			"| [[ColdFusion]], [[PHP]]\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}, but not mandatory\n" +
			"| Push\n" +
			"| {{No}}, custom\n" +
			"| {{Yes}}, via lexicons for [http://compoundtheory.com/transfer Transfer] and [http://www.reactorframework.org Reactor]\n" +
			"| {{Yes}}, [[CFUnit]], [[CFCUnit]]\n" +
			"|\n" +
			"| {{Yes}}, multiple plugins available\n" +
			"|\n" +
			"| {{Yes}}, via lexicon for [http://coldspringframework.org ColdSpring]\n" +
			"| {{Yes}}, via qforms or built in cf validation\n" +
			"|-\n" +
			"| [http://mdp.cti.depaul.edu/examples Gluon]\n" +
			"| Python\n" +
			"| Via third party libraries\n" +
			"| {{Yes}}\n" +
			"| Push\n" +
			"| {{Yes}} provided by framework\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"| Via third party wsgi modules\n" +
			"| {{Yes}}\n" +
			"|-\n" +
			"|[[Grails (Framework)|Grails]]\n" +
			"| [[Groovy (programming language)|Groovy]]\n" +
			"| {{Yes}}\n" +
			"| [[Active record pattern|ActiveRecord]]\n" +
			"| Push\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}, GORM, [[Hibernate (Java)|Hibernate]]\n" +
			"| {{Yes}}, [[Unit Test|Unit Test]]\n" +
			"| {{No}}\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"|-\n" +
			"|[[InterJinn]]\n" +
			"| PHP\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"| Push & Pull\n" +
			"| {{No}}, custom\n" +
			"| {{No}}\n" +
			"|\n" +
			"|\n" +
			"|\n" +
			"| {{Yes}}, TemplateJinn\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}, FormJinn\n" +
			"|-\n" +
			"|[[JBoss Seam]]\n" +
			"| Java\n" +
			"| {{Yes}}\n" +
			"| \n" +
			"| Pull\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}, [[Java Persistence API|JPA]], [[Hibernate (Java)|Hibernate]]\n" +
			"| {{Yes}}, [[JUnit]], [[TestNG]]\n" +
			"|\n" +
			"| {{Yes}}, [[JAAS]] integration\n" +
			"| {{Yes}}, [[Facelets]]\n" +
			"|\n" +
			"| {{Yes}}, [[Hibernate Validator]]\n" +
			"|-\n" +
			"| [[Jifty]]\n" +
			"| Perl\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"| Push\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}, [[Jifty::DBI]]\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}, [[Mason (Perl)|Mason]], Template::Declare\n" +
			"| {{Yes}}, Memcached\n" +
			"| {{Yes}}\n" +
			"|-\n" +
			"|-\n" +
			"| [[Kumbia]]\n" +
			"| PHP5\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"| x\n" +
			"| x\n" +
			"| [[Active record pattern|ActiveRecord]]\n" +
			"| x\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}, [[Smarty]]\n" +
			"| {{Yes}}, Memcached\n" +
			"| {{Yes}}\n" +
			"|-\n" +
			"|[[Mach-II]]\n" +
			"| [[ColdFusion]]\n" +
			"| {{Yes}}, via CF or any JavaScript Library\n" +
			"| {{Yes}}\n" +
			"| Push\n" +
			"| {{Yes}}, via custom plugin\n" +
			"| {{Yes}} [[Transfer]], [[Reactor]], [[Hibernate]]\n" +
			"| {{Yes}}, [[CFUnit]], [[CFCUnit]]\n" +
			"| \n" +
			"| {{Yes}}, via plugin\n" +
			"|\n" +
			"| {{Yes}}, [[ColdSpring]]\n" +
			"| \n" +
			"|-\n" +
			"|[[Model-Glue::Unity]]\n" +
			"| [[ColdFusion]]\n" +
			"| {{Yes}}, via CF or any JavaScript Library\n" +
			"| {{Yes}}\n" +
			"| Push\n" +
			"| {{Yes}}, via custom plugin\n" +
			"| {{Yes}} [[Transfer]], [[Reactor]], [[Hibernate]]\n" +
			"| {{Yes}}, [[CFUnit]], [[CFCUnit]]\n" +
			"|\n" +
			"| {{Yes}}, via plugin\n" +
			"| \n" +
			"| {{Yes}}, [[Coldspring]]\n" +
			"| {{Yes}}\n" +
			"|-\n" +
			"|[[Monorail (.Net)|MonoRail]]\n" +
			"| .NET\n" +
			"| {{Yes}}, [[Prototype JavaScript Framework|Prototype]]\n" +
			"| [[Active record pattern|ActiveRecord]]\n" +
			"| Push\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}, [[Active record pattern|ActiveRecord]]\n" +
			"| {{Yes}}, [[Unit testing|Unit Tests]]\n" +
			"|\n" +
			"| {{Yes}}, via [[ASP.NET]] Forms Authentication\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"|-\n" +
			"|[[Nitro (web framework)|Nitro]]\n" +
			"| Ruby\n" +
			"| {{Yes}}, [[jQuery]]\n" +
			"| {{Yes}}\n" +
			"| Push\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}, [[Nitro (web framework)#Og|Og]]\n" +
			"| {{Yes}}, [[RSpec]]\n" +
			"| {{Yes}} (automatic)\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"|-\n" +
			"|[[PHPulse]]\n" +
			"| PHP\n" +
			"| {{Yes}},\n" +
			"| {{Yes}},\n" +
			"| Push\n" +
			"| {{Yes}},\n" +
			"| {{Yes}},\n" +
			"| {{Yes}}, [[Unit Testing|Unit Tests]]\n" +
			"|\n" +
			"| {{Yes}}, [[Access control list|ACL-based]]\n" +
			"| {{Yes}}, [[Smarty]]\n" +
			"| {{Yes}}, multiple (Memcached, TurckMM, shared memory,...)\n" +
			"| {{Yes}}\n" +
			"|-\n" +
			"|[[PRADO]]\n" +
			"| PHP5\n" +
			"| {{Yes}}, [[Active Controls]]\n" +
			"| {{Yes}}\n" +
			"| Push\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}, [[ActiveRecord]], [[SQLMap]]\n" +
			"| {{Yes}}, [[PHPUnit]], [[SimpleTest]], [[Selenium]]\n" +
			"|\n" +
			"| {{Yes}}, modular and role-based ACL\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"|-\n" +
			"|[[Pylons (web framework)|Pylons]]\n" +
			"| Python\n" +
			"| {{Yes}}, helpers for [[Prototype JavaScript Framework|Prototype]] and [[script.aculo.us]]\n" +
			"| {{Yes}}\n" +
			"| Push\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}, [[SQLObject]], [[SQLAlchemy]]\n" +
			"| {{Yes}}, via nose\n" +
			"|\n" +
			"|\n" +
			"| {{Yes}}, pluggable (mako, genshi, mighty, kid, ...)\n" +
			"| {{Yes}}, Beaker cache (memory, memcached, file, databases)\n" +
			"| {{Yes}}, preferred formencode\n" +
			"|-\n" +
			"| [[Qcodo]]\n" +
			"| PHP5\n" +
			"| {{Yes}}, built-in\n" +
			"| {{Yes}}, QControl\n" +
			"| Push\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}, Code Generation-based\n" +
			"|\n" +
			"|\n" +
			"|\n" +
			"| {{Yes}}, QForm and QControl\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"|-\n" +
			"|[[RIFE]]\n" +
			"| Java\n" +
			"| {{Yes}}, [[DWR|DWR (Java)]]\n" +
			"| {{Yes}}\n" +
			"| Push & Pull\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}, Out of container testing\n" +
			"| \n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}, [[Terracotta Cluster|Integration with Terracotta]]\n" +
			"| {{Yes}}\n" +
			"|-\n" +
			"|[[Ruby on Rails]]\n" +
			"| Ruby\n" +
			"| {{Yes}}, [[Prototype JavaScript Framework|Prototype]], [[script.aculo.us]]\n" +
			"| [[Active record pattern|ActiveRecord]], [[Ruby on Rails|Action Pack]]\n" +
			"| Push\n" +
			"| {{Yes}}, Localization Plug-in\n" +
			"| {{Yes}}, [[Active record pattern|ActiveRecord]]\n" +
			"| {{Yes}}, [[Unit testing|Unit Tests]], Functional Tests and Integration Tests\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}, Plug-in\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"|-\n" +
			"|[[Seaside web framework|Seaside]]\n" +
			"| Smalltalk\n" +
			"| {{Yes}}, [[Prototype JavaScript Framework|Prototype]], [[script.aculo.us]]\n" +
			"| \n" +
			"|\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}, [[GLORP]], [[Gemstone Database Management System|Gemstone/S]], ...\n" +
			"|\n" +
			"|\n" +
			"|\n" +
			"|\n" +
			"|\n" +
			"|\n" +
			"|-\n" +
			"|[[Stripes]]\n" +
			"| Java\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"| Push\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}, [[Hibernate (Java)|Hibernate]]\n" +
			"| {{Yes}}\n" +
			"| \n" +
			"| {{Yes}}, framework extension\n" +
			"| {{Yes}}\n" +
			"| \n" +
			"| {{Yes}}\n" +
			"|-\n" +
			"|-\n" +
			"|[[Symfony]]\n" +
			"| PHP5\n" +
			"| {{Yes}}, [[Prototype JavaScript Framework|Prototype]], [[script.aculo.us]], Unobtrusive Ajax with UJS and PJS plugins\n" +
			"| {{Yes}}\n" +
			"| Push\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}, [[Propel (PHP)|Propel]], [[Doctrine O/RM|Doctrine]]\n" +
			"| {{Yes}}\n" +
			"| Plugin exists (alpha code, though)\n" +
			"| {{Yes}}, plugin\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"|-\n" +
			"|[[Tigermouse]]\n" +
			"| PHP5\n" +
			"| {{Yes}}, it is mostly Ajax-only framework\n" +
			"| {{Yes}}, [[Active record pattern|ActiveRecord]]\n" +
			"| Push\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}, [[Active record pattern|ActiveRecord]]\n" +
			"| {{No}}\n" +
			"| {{No}}, Multiple RBMSes and access libraries supported\n" +
			"| {{Yes}}, through intercepting filters ([[Access control list|ACL-based]], customizable)\n" +
			"| {{Yes}}\n" +
			"| {{No}}\n" +
			"| {{Yes}}\n" +
			"|-\n" +
			"|[[TurboGears]]\n" +
			"| Python\n" +
			"| {{Yes}}\n" +
			"| \n" +
			"|\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}, [[SQLObject]], [[SQLAlchemy]]\n" +
			"|\n" +
			"|\n" +
			"|\n" +
			"|\n" +
			"|\n" +
			"|\n" +
			"|-\n" +
			"|[[WebLEAF]]\n" +
			"| Java\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"| Push\n" +
			"| {{Yes}}\n" +
			"| {{Yes}} [[Hibernate]], [[EJB]]\n" +
			"|\n" +
			"|\n" +
			"| {{Yes}}, extensible through custom interfaces\n" +
			"| {{Yes}} [[XSLT]], [[FreeMarker]]\n" +
			"|\n" +
			"|\n" +
			"|-\n" +
			"|[[WebObjects]]\n" +
			"| Java\n" +
			"| {{Yes}}\n" +
			"| \n" +
			"|\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}, [[Enterprise Objects Framework|EOF]]\n" +
			"|\n" +
			"|\n" +
			"|\n" +
			"|\n" +
			"|\n" +
			"|\n" +
			"|-\n" +
			"|[[Zend Framework]]\n" +
			"| PHP5 (>=5.1.4)\n" +
			"| {{Yes}}, [[JavaScript library|various libraries]]\n" +
			"| {{Yes}}\n" +
			"| Push\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}, Table and Row data gateway\n" +
			"| {{Yes}}, [[Unit testing|Unit Tests]]\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}, [[Access control list|ACL-based]]\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"|-\n" +
			"|[[Zope]]2\n" +
			"| Python\n" +
			"|\n" +
			"| {{Yes}}\n" +
			"| Pull\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}, native OODBMS called [[Zope Object Database|ZODB]], [[SQLObject]], [[SQLAlchemy]]\n" +
			"| {{Yes}}, [[Unit testing|Unit Tests]]\n" +
			"| \n" +
			"| {{Yes}}, [[Access control list|ACL-based]]\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}, CMFFormController\n" +
			"|-\n" +
			"|[[Zope]]3\n" +
			"| Python\n" +
			"| {{Yes}}, via add-on products, e.g. [[Plone_(software)|Plone]] w/KSS \n" +
			"| {{Yes}}\n" +
			"| Pull\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}, native OODBMS called [[Zope Object Database|ZODB]], [[SQLObject]], [[SQLAlchemy]]\n" +
			"| {{Yes}}, [[Unit testing|Unit Tests]], Functional Tests\n" +
			"| {{Yes}}, ZODB generations\n" +
			"| {{Yes}}, [[Access control list|ACL-based]]\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"| {{Yes}}\n" +
			"|-class=\"sortbottom\"\n" +
			"!Project\n" +
			"!Language\n" +
			"![[Ajax (programming)|Ajax]]\n" +
			"![[Model-view-controller|MVC]] framework\n" +
			"![[Web_application_framework#Push-based_vs._Pull-based|MVC Push/Pull]]\n" +
			"![[Internationalization_and_localization|i18n & l10n?]]\n" +
			"![[Object-relational mapping|ORM]]\n" +
			"!Testing framework(s)\n" +
			"!DB migration framework(s)\n" +
			"!Security Framework(s)\n" +
			"!Template Framework(s)\n" +
			"!Caching Framework(s)\n" +
			"!Form Validation Framework(s)\n" +
			"|}";

		Table t = parse(content);

		System.out.println(t);
		Row row = t.getRows()[1];
		assertEquals(13, row.getCells().length);
		for (int i = 0; i < row.getCells().length; i++) {
			assertNull(row.getCells()[i].getOptions());
		}
		assertEquals("[[Agavi_(framework)|Agavi]]", row.getCells()[0].getContent()[0].toString());
		assertEquals(" PHP", row.getCells()[1].getContent()[0].toString());
		assertEquals(" ", row.getCells()[2].getContent()[0].toString());
		assertEquals(" {{Yes}}", row.getCells()[3].getContent()[0].toString());
		assertEquals(" Push", row.getCells()[4].getContent()[0].toString());
		assertEquals(" {{Yes}}", row.getCells()[5].getContent()[0].toString());
		assertEquals(" {{Yes}}", row.getCells()[6].getContent()[0].toString());
		assertEquals(" {{Yes}}", row.getCells()[7].getContent()[0].toString());
		assertEquals(" {{Yes}}", row.getCells()[8].getContent()[0].toString());
		assertEquals(" {{Yes}}", row.getCells()[9].getContent()[0].toString());
		assertEquals(" {{Yes}}", row.getCells()[10].getContent()[0].toString());
		assertEquals(" {{Yes}}", row.getCells()[11].getContent()[0].toString());
		assertEquals(" {{Yes}}", row.getCells()[12].getContent()[0].toString());

	}

	public void testLanguagesTable() throws Exception {
		String content =
			"{| class=\"wikitable sortable\"\n" +
			"|- style=\"background-color: #efefef;\"\n" +
			"!639-1!!639-2!!639-3!!Language name!!Native name!!comment\n" +
			"|-\n" +
			"|aa||aar||aar||[[Afar language|Afar]]||lang=\"aa\"|Afaraf||\n" +
			"|-\n" +
			"|ab||abk||abk||[[Abkhaz language|Abkhazian]]||lang=\"ab\"|�?ҧ�?уа||\n" +
			"|-\n" +
			"|ae||ave||ave||[[Avestan language|Avestan]]||lang=\"ae\"|avesta||\n" +
			"|-\n" +
			"|af||afr||afr||[[Afrikaans]]||lang=\"af\"|Afrikaans||\n" +
			"|-\n" +
			"|ak||aka||aka + [[ISO 639:aka|2]]||[[Akan languages|Akan]]||lang=\"ak\"|Akan||\n" +
			"|-\n" +
			"|am||amh||amh||[[Amharic language|Amharic]]||lang=\"am\"|አማርኛ||\n" +
			"|-\n" +
			"|an||arg||arg||[[Aragonese language|Aragonese]]||lang=\"an\"|Aragonés||\n" +
			"|-\n" +
			"|ar||ara||ara + [[ISO 639:ara|30]]||[[Arabic language|Arabic]]||lang=\"ar\" dir=\"rtl\"|‫العربية||Standard Arabic is [arb]\n" +
			"|-\n" +
			"|as||asm||asm||[[Assamese language|Assamese]]||lang=\"as\"|অসমীয়া||\n" +
			"|-\n" +
			"|av||ava||ava||[[Avar language|Avaric]]||lang=\"av\"|авар мацӀ; магӀарул мацӀ||\n" +
			"|-\n" +
			"|ay||aym||aym + [[ISO 639:aym|2]]||[[Aymara language|Aymara]]||lang=\"ay\"|aymar aru||\n" +
			"|-\n" +
			"|az||aze||aze + [[ISO 639:aze|2]]||[[Azerbaijani language|Azerbaijani]]||lang=\"az\"|azərbaycan dili||\n" +
			"|-\n" +
			"|ba||bak||bak||[[Bashkir language|Bashkir]]||lang=\"ba\"|башҡорт теле||\n" +
			"|-\n" +
			"|be||bel||bel||[[Belarusian language|Belarusian]]||lang=\"be\"|Белару�?ка�?||\n" +
			"|-\n" +
			"|bg||bul||bul||[[Bulgarian language|Bulgarian]]||lang=\"bg\"|българ�?ки език||\n" +
			"|-\n" +
			"|bh||bih||--||[[Bihari languages|Bihari]]||lang=\"bh\"|भोजप�?री||collective language code for Bhojpuri, Magahi, and Maithili\n" +
			"|-\n" +
			"|bi||bis||bis||[[Bislama language|Bislama]]||lang=\"bi\"|Bislama||\n" +
			"|-\n" +
			"|bm||bam||bam||[[Bambara language|Bambara]]||lang=\"bm\"|bamanankan||\n" +
			"|-\n" +
			"|bn||ben||ben||[[Bengali language|Bengali]]||lang=\"bn\"|বাংলা||\n" +
			"|-\n" +
			"|bo||tib/bod||bod||[[Tibetan language|Tibetan]]||lang=\"bo\"|བོད་ཡིག||\n" +
			"|-\n" +
			"|br||bre||bre||[[Breton language|Breton]]||lang=\"br\"|brezhoneg||\n" +
			"|-\n" +
			"|bs||bos||bos||[[Bosnian language|Bosnian]]||lang=\"bs\"|bosanski jezik||\n" +
			"|-\n" +
			"|ca||cat||cat||[[Catalan language|Catalan]]||lang=\"ca\"|Català||\n" +
			"|-\n" +
			"|ce||che||che||[[Chechen language|Chechen]]||lang=\"ce\"|нохчийн мотт||\n" +
			"|-\n" +
			"|ch||cha||cha||[[Chamorro language|Chamorro]]||lang=\"ch\"|Chamoru||\n" +
			"|-\n" +
			"|co||cos||cos||[[Corsican language|Corsican]]||lang=\"co\"|corsu; lingua corsa||\n" +
			"|-\n" +
			"|cr||cre||cre + [[ISO 639:cre|6]]||[[Cree language|Cree]]||lang=\"cr\"|ᓀ�?��?�ᔭ�??�??�?�||\n" +
			"|-\n" +
			"|cs||cze/ces||ces||[[Czech language|Czech]]||lang=\"cs\"|�?esky; �?eština||\n" +
			"|-\n" +
			"|cu||chu||chu||[[Old Church Slavonic|Church Slavic]]||||\n" +
			"|-\n" +
			"|cv||chv||chv||[[Chuvash language|Chuvash]]||lang=\"cv\"|чӑваш чӗлхи||\n" +
			"|-\n" +
			"|cy||wel/cym||cym||[[Welsh language|Welsh]]||lang=\"cy\"|Cymraeg||\n" +
			"|-\n" +
			"|da||dan||dan||[[Danish language|Danish]]||lang=\"da\"|dansk||\n" +
			"|-\n" +
			"|de||ger/deu||deu||[[German language|German]]||lang=\"de\"|Deutsch||\n" +
			"|-\n" +
			"|dv||div||div||[[Dhivehi language|Divehi]]||lang=\"dv\" dir=\"rtl\"|‫ދިވެހި||\n" +
			"|-\n" +
			"|dz||dzo||dzo||[[Dzongkha language|Dzongkha]]||lang=\"dz\"|རྫོང་�?||\n" +
			"|-\n" +
			"|ee||ewe||ewe||[[Ewe language|Ewe]]||lang=\"ee\"|�?ʋɛgbɛ||\n" +
			"|-\n" +
			"|el||gre/ell||ell||[[Greek language|Greek]]||lang=\"el\"|Ελληνικά||\n" +
			"|-\n" +
			"|en||eng||eng||[[English language|English]]||lang=\"en\"|English||\n" +
			"|-\n" +
			"|eo||epo||epo||[[Esperanto]]||lang=\"eo\"|Esperanto||\n" +
			"|-\n" +
			"|es||spa||spa||[[Spanish language|Spanish]]||lang=\"es\"|español; castellano||\n" +
			"|-\n" +
			"|et||est||est||[[Estonian language|Estonian]]||lang=\"et\"|Eesti keel||\n" +
			"|-\n" +
			"|eu||baq/eus||eus||[[Basque language|Basque]]||lang=\"eu\"|euskara||\n" +
			"|-\n" +
			"|fa||per/fas||fas + [[ISO 639:fas|2]]||[[Persian language|Persian]]||lang=\"fa\" dir=\"rtl\"|‫�?ارسی||\n" +
			"|-\n" +
			"|ff||ful||ful + [[ISO 639:ful|9]]||[[Fula language|Fulah]]||lang=\"ff\"|Fulfulde||\n" +
			"|-\n" +
			"|fi||fin||fin||[[Finnish language|Finnish]]||lang=\"fi\"|suomen kieli||\n" +
			"|-\n" +
			"|fj||fij||fij||[[Fijian language|Fijian]]||lang=\"fj\"|vosa Vakaviti||\n" +
			"|-\n" +
			"|fo||fao||fao||[[Faroese language|Faroese]]||lang=\"fo\"|Føroyskt||\n" +
			"|-\n" +
			"|fr||fre/fra||fra||[[French language|French]]||lang=\"fr\"|français; langue française||\n" +
			"|-\n" +
			"|fy||fry||fry + [[ISO 639:fry|3]]||[[West Frisian language|Western Frisian]]||lang=\"fy\"|Frysk||\n" +
			"|-\n" +
			"|ga||gle||gle||[[Irish language|Irish]]||lang=\"ga\"|Gaeilge||\n" +
			"|-\n" +
			"|gd||gla||gla||[[Scottish Gaelic language|Scottish Gaelic]]||lang=\"gd\"|Gàidhlig||\n" +
			"|-\n" +
			"|gl||glg||glg||[[Galician language|Galician]]||lang=\"gl\"|Galego||\n" +
			"|-\n" +
			"|gn||grn||grn + [[ISO 639:grn|5]]||[[Guaraní language|Guaraní]]||lang=\"gn\"|Avañe'ẽ||\n" +
			"|-\n" +
			"|gu||guj||guj||[[Gujarati language|Gujarati]]||lang=\"gu\"|ગ�?જરાતી||\n" +
			"|-\n" +
			"|gv||glv||glv||[[Manx language|Manx]]||lang=\"gv\"|Ghaelg||\n" +
			"|-\n" +
			"|ha||hau||hau||[[Hausa language|Hausa]]||lang=\"ha\" dir=\"rtl\"|‫هَو�?سَ||\n" +
			"|-\n" +
			"|he||heb||heb||[[Hebrew language|Hebrew]]||lang=\"he\" dir=\"rtl\"|‫עברית||\n" +
			"|-\n" +
			"|hi||hin||hin||[[Hindi]]||lang=\"hi\"|हिन�?दी; हिंदी||\n" +
			"|-\n" +
			"|ho||hmo||hmo||[[Hiri Motu language|Hiri Motu]]||lang=\"ho\"|Hiri Motu||\n" +
			"|-\n" +
			"|hr||scr/hrv||hrv||[[Croatian language|Croatian]]||lang=\"hr\"|Hrvatski||\n" +
			"|-\n" +
			"|ht||hat||hat||[[Haitian Creole language|Haitian]]||lang=\"ht\"|Kreyòl ayisyen||\n" +
			"|-\n" +
			"|hu||hun||hun||[[Hungarian language|Hungarian]]||lang=\"hu\"|Magyar||\n" +
			"|-\n" +
			"|hy||arm/hye||hye||[[Armenian language|Armenian]]||lang=\"hy\"|Հայերեն||\n" +
			"|-\n" +
			"|hz||her||her||[[Herero language|Herero]]||lang=\"hz\"|Otjiherero||\n" +
			"|-\n" +
			"|ia||ina||ina||[[Interlingua|Interlingua (International Auxiliary Language Association)]]||lang=\"ia\"|interlingua||\n" +
			"|-\n" +
			"|id||ind||ind||[[Indonesian language|Indonesian]]||lang=\"id\"|Bahasa Indonesia||\n" +
			"|-\n" +
			"|ie||ile||ile||[[Interlingue language|Interlingue]]||lang=\"ie\"|Interlingue||\n" +
			"|-\n" +
			"|ig||ibo||ibo||[[Igbo language|Igbo]]||lang=\"ig\"|Igbo||\n" +
			"|-\n" +
			"|ii||iii||iii||[[Yi language|Sichuan Yi]]||lang=\"ii\"|ꆇꉙ||\n" +
			"|-\n" +
			"|ik||ipk||ipk + [[ISO 639:ipk|2]]||[[Inupiaq language|Inupiaq]]||lang=\"ik\"|Iñupiaq; Iñupiatun||\n" +
			"|-\n" +
			"|io||ido||ido||[[Ido]]||lang=\"io\"|Ido||\n" +
			"|-\n" +
			"|is||ice/isl||isl||[[Icelandic language|Icelandic]]||lang=\"is\"|�?slenska||\n" +
			"|-\n" +
			"|it||ita||ita||[[Italian language|Italian]]||lang=\"it\"|Italiano||\n" +
			"|-\n" +
			"|iu||iku||iku + [[ISO 639:iku|2]]||[[Inuktitut]]||lang=\"iu\"|�?�ᓄᒃᑎ�?ᑦ||\n" +
			"|-\n" +
			"|ja||jpn||jpn||[[Japanese language|Japanese]]||{{lang|ja-Hani|日本語}} ({{lang|ja-Hira|�?��?�ん�?��?�?��?��?�ん�?�}})||\n" +
			"|-\n" +
			"|jv||jav||jav||[[Javanese language|Javanese]]||lang=\"jv\"|basa Jawa||\n" +
			"|-\n" +
			"|ka||geo/kat||kat||[[Georgian language|Georgian]]||lang=\"ka\"|ქ�?რთული||\n" +
			"|-\n" +
			"|kg||kon||kon + [[ISO 639:kon|3]]||[[Kongo language|Kongo]]||lang=\"kg\"|KiKongo||\n" +
			"|-\n" +
			"|ki||kik||kik||[[Gikuyu language|Kikuyu]]||lang=\"ki\"|Gĩkũyũ||\n" +
			"|-\n" +
			"|kj||kua||kua||[[Kwanyama|Kwanyama]]||lang=\"kj\"|Kuanyama||\n" +
			"|-\n" +
			"|kk||kaz||kaz||[[Kazakh language|Kazakh]]||lang=\"kk\"|Қазақ тілі||\n" +
			"|-\n" +
			"|kl||kal||kal||[[Kalaallisut language|Kalaallisut]]||lang=\"kl\"|kalaallisut; kalaallit oqaasii||\n" +
			"|-\n" +
			"|km||khm||khm||[[Khmer language|Khmer]]||lang=\"km\"|ភាសា�?្មែរ||\n" +
			"|-\n" +
			"|kn||kan||kan||[[Kannada language|Kannada]]||lang=\"kn\"|ಕನ�?ನಡ||\n" +
			"|-\n" +
			"|ko||kor||kor||[[Korean language|Korean]]||{{lang|ko-Hang|한국어}} ({{lang|ko-Hani|韓國語}}); {{lang|ko-Hang|조선�?}} ({{lang|ko-Hani|�?鮮語}})<!-- ideograph is used in Korea-->||\n" +
			"|-\n" +
			"|kr||kau||kau + [[ISO 639:kau|3]]||[[Kanuri language|Kanuri]]||lang=\"kr\"|Kanuri||\n" +
			"|-\n" +
			"|ks||kas||kas||[[Kashmiri language|Kashmiri]]||{{lang|ks-Deva|कश�?मीरी}}; {{rtl-lang|ks-Arb|كشميري}}||\n" +
			"|-\n" +
			"|ku||kur||kur + [[ISO 639:kur|3]]||[[Kurdish language|Kurdish]]||{{lang|ku-Latn|Kurdî}}; {{rtl-lang|ku-Arab|كوردی}}||\n" +
			"|-\n" +
			"|kv||kom||kom + [[ISO 639:kom|2]]||[[Komi language|Komi]]||lang=\"kv\"|коми кыв||\n" +
			"|-\n" +
			"|kw||cor||cor||[[Cornish language|Cornish]]||lang=\"kw\"|Kernewek||\n" +
			"|-\n" +
			"|ky||kir||kir||[[Kyrgyz language|Kirghiz]]||lang=\"ky\"|кыргыз тили||\n" +
			"|-\n" +
			"|la||lat||lat||[[Latin]]||lang=\"la\"|latine; lingua latina||\n" +
			"|-\n" +
			"|lb||ltz||ltz||[[Luxembourgish language|Luxembourgish]]||lang=\"lb\"|Lëtzebuergesch||\n" +
			"|-\n" +
			"|lg||lug||lug||[[Luganda language|Ganda]]||lang=\"lg\"|Luganda||\n" +
			"|-\n" +
			"|li||lim||lim||[[Limburgish language|Limburgish]]||lang=\"li\"|Limburgs||\n" +
			"|-\n" +
			"|ln||lin||lin||[[Lingala language|Lingala]]||lang=\"ln\"|Lingála||\n" +
			"|-\n" +
			"|lo||lao||lao||[[Lao language|Lao]]||lang=\"lo\"|ພາສາລາວ||\n" +
			"|-\n" +
			"|lt||lit||lit||[[Lithuanian language|Lithuanian]]||lang=\"lt\"|lietuvių kalba||\n" +
			"|-\n" +
			"|lu||lub||lub||[[Tshiluba language|Luba-Katanga]]||||\n" +
			"|-\n" +
			"|lv||lav||lav||[[Latvian language|Latvian]]||lang=\"lv\"|latviešu valoda||\n" +
			"|-\n" +
			"|mg||mlg||mlg + [[ISO 639:mlg|10]]||[[Malagasy language|Malagasy]]||lang=\"mg\"|Malagasy fiteny||\n" +
			"|-\n" +
			"|mh||mah||mah||[[Marshallese language|Marshallese]]||lang=\"mh\"|Kajin M̧ajeļ||\n" +
			"|-\n" +
			"|mi||mao/mri||mri||[[M�?ori language|M�?ori]]||lang=\"mi\"|te reo M�?ori||\n" +
			"|-\n" +
			"|mk /sl ||mac/mkd||mkd||[[Macedonian language|Macedonian]]||lang=\"mk\"|македон�?ки јазик||\n" +
			"|-\n" +
			"|ml||mal||mal||[[Malayalam language|Malayalam]]||lang=\"ml\"|മലയാളം||\n" +
			"|-\n" +
			"|mn||mon||mon + [[ISO 639:mon|2]]||[[Mongolian language|Mongolian]]||lang=\"mn\"|Монгол||\n" +
			"|-\n" +
			"|mo||mol||mol||[[Moldovan language|Moldavian]]||lang=\"mo\"|лимба молдовен�?�?к�?||\n" +
			"|-\n" +
			"|mr||mar||mar||[[Marathi language|Marathi]]||lang=\"mr\"|मराठी||\n" +
			"|-\n" +
			"|ms||may/msa||msa + [[ISO 639:msa|13]]||[[Malay language|Malay]]||{{lang|ms|bahasa Melayu}}; {{rtl-lang|ms-Arab|بهاس ملايو}} ||Malay (specific) is [mly]\n" +
			"|-\n" +
			"|mt||mlt||mlt||[[Maltese language|Maltese]]||lang=\"mt\"|Malti||\n" +
			"|-\n" +
			"|my||bur/mya||mya||[[Burmese language|Burmese]]||lang=\"my\"|ဗမာစာ||\n" +
			"|-\n" +
			"|na||nau||nau||[[Nauruan language|Nauru]]||lang=\"na\"|Ekakairũ Naoero||\n" +
			"|-\n" +
			"|nb||nob||nob||[[Bokmål|Norwegian Bokmål]]||lang=\"nb\"|Norsk bokmål||\n" +
			"|-\n" +
			"|nd||nde||nde||[[Northern Ndebele language|North Ndebele]]||lang=\"nd\"|isiNdebele||\n" +
			"|-\n" +
			"|ne||nep||nep||[[Nepali language|Nepali]]||lang=\"ne\"|नेपाली||\n" +
			"|-\n" +
			"|ng||ndo||ndo||[[Ndonga]]||lang=\"ng\"|Owambo||\n" +
			"|-\n" +
			"|nl||dut/nld||nld||[[Dutch language|Dutch]]||lang=\"nl\"|Nederlands||\n" +
			"|-\n" +
			"|nn||nno||nno||[[Nynorsk|Norwegian Nynorsk]]||lang=\"nn\"|Norsk nynorsk||\n" +
			"|-\n" +
			"|no||nor||nor + [[ISO 639:nor|2]]||[[Norwegian language|Norwegian]]||lang=\"no\"|Norsk||\n" +
			"|-\n" +
			"|nr||nbl||nbl||[[Southern Ndebele language|South Ndebele]]||lang=\"nr\"|Ndébélé||\n" +
			"|-\n" +
			"|nv||nav||nav||[[Navajo language|Navajo]]||lang=\"nv\"|Diné bizaad; Dinékʼehǰí||\n" +
			"|-\n" +
			"|ny||nya||nya||[[Chichewa language|Chichewa]]||lang=\"ny\"|chiCheŵa; chinyanja||\n" +
			"|-\n" +
			"|oc||oci||oci + [[ISO 639:oci|5]]||[[Occitan language|Occitan]]||lang=\"oc\"|Occitan||\n" +
			"|-\n" +
			"|oj||oji||oji + [[ISO 639:oji|7]]||[[Anishinaabe language|Ojibwa]]||lang=\"oj\"|�?�ᓂᔑᓈ�?�ᒧ�?��?||\n" +
			"|-\n" +
			"|om||orm||orm + [[ISO 639:orm|4]]||[[Oromo language|Oromo]]||lang=\"om\"|Afaan Oromoo||\n" +
			"|-\n" +
			"|or||ori||ori||[[Oriya language|Oriya]]||lang=\"or\"|ଓଡ଼ିଆ||\n" +
			"|-\n" +
			"|os||oss||oss||[[Ossetic language|Ossetian]]||lang=\"os\"|Ирон æвзаг||\n" +
			"|-\n" +
			"|pa||pan||pan||[[Punjabi language|Panjabi]]||{{lang|pa|ਪੰਜਾਬੀ}}; {{rtl-lang|pa-Arab|پنجابی}}||\n" +
			"|-\n" +
			"|pi||pli||pli||[[P�?li language|P�?li]]||lang=\"pi\"|पािऴ||\n" +
			"|-\n" +
			"|pl||pol||pol||[[Polish language|Polish]]||lang=\"pl\"|polski||\n" +
			"|-\n" +
			"|ps||pus||pus + [[ISO 639:pus|3]]||[[Pashto language|Pashto]]||lang=\"ps\" dir=\"rtl\"|‫پښتو||\n" +
			"|-\n" +
			"|pt||por||por||[[Portuguese language|Portuguese]]||lang=\"pt\"|Português||\n" +
			"|-\n" +
			"|qu||que||que + [[ISO 639:que|44]]||[[Quechua]]||lang=\"qu\"|Runa Simi; Kichwa||\n" +
			"|-\n" +
			"|rm||roh||roh||[[Romansh|Raeto-Romance]]||lang=\"rm\"|rumantsch grischun||\n" +
			"|-\n" +
			"|rn||run||run||[[Kirundi language|Kirundi]]||lang=\"rn\"|kiRundi||\n" +
			"|-\n" +
			"|ro||rum/ron||ron||[[Romanian language|Romanian]]||lang=\"ro\"|română||\n" +
			"|-\n" +
			"|ru||rus||rus||[[Russian language|Russian]]||lang=\"ru\"|ру�?�?кий �?зык||\n" +
			"|-\n" +
			"|rw||kin||kin||[[Kinyarwanda language|Kinyarwanda]]||lang=\"rw\"|Kinyarwanda||\n" +
			"|-\n" +
			"|ry||sla/rue||rue||[[Rusyn language|Rusyn]]||lang=\"ry\"|ру�?ин�?ькый �?зык||\n" +
			"|-\n" +
			"|sa||san||san||[[Sanskrit]]||lang=\"sa\"|संस�?कृतम�?||\n" +
			"|-\n" +
			"|sc||srd||srd + [[ISO 639:srd|4]]||[[Sardinian language|Sardinian]]||lang=\"sc\"|sardu||\n" +
			"|-\n" +
			"|sd||snd||snd||[[Sindhi language|Sindhi]]||{{lang|sd-Deva|सिन�?धी}}; {{rtl-lang|sd-Arab|‫سنڌي، سندھی}}||\n" +
			"|-\n" +
			"|se||sme||sme||[[Northern Sami]]||lang=\"se\"|Davvisámegiella||\n" +
			"|-\n" +
			"|sg||sag||sag||[[Sango language|Sango]]||lang=\"sg\"|yângâ tî sängö||\n" +
			"|-\n" +
			"|sh||--||hbs + [[ISO 639:hbs|3]]||[[Serbo-Croatian]]||lang=\"sh\"|Srpskohrvatski/Срп�?кохрват�?ки||\n" +
			"|-\n" +
			"|si||sin||sin||[[Sinhalese language|Sinhalese]]||lang=\"si\"|සිංහල||\n" +
			"|-\n" +
			"|sk||slo/slk||slk||[[Slovak language|Slovak]]||lang=\"sk\"|sloven�?ina||\n" +
			"|-\n" +
			"|sl||slv||slv||[[Slovenian language|Slovenian]]||lang=\"sl\"|slovenš�?ina||\n" +
			"|-\n" +
			"|sm||smo||smo||[[Samoan language|Samoan]]||lang=\"sm\"|gagana fa'a Samoa||\n" +
			"|-\n" +
			"|sn||sna||sna||[[Shona language|Shona]]||lang=\"sn\"|chiShona||\n" +
			"|-\n" +
			"|so||som||som||[[Somali language|Somali]]||lang=\"so\"|Soomaaliga; af Soomaali||\n" +
			"|-\n" +
			"|sq||alb/sqi||sqi + [[ISO 639:sqi|4]]||[[Albanian language|Albanian]]||lang=\"sq\"|Shqip||\n" +
			"|-\n" +
			"|sr||scc/srp||srp||[[Serbian language|Serbian]]||lang=\"sr\"|�?рп�?ки језик||\n" +
			"|-\n" +
			"|ss||ssw||ssw||[[Swati language|Swati]]||lang=\"ss\"|SiSwati||\n" +
			"|-\n" +
			"|st||sot||sot||[[Sotho language|Sotho]]||lang=\"st\"|seSotho||\n" +
			"|-\n" +
			"|su||sun||sun||[[Sundanese language|Sundanese]]||lang=\"su\"|Basa Sunda||\n" +
			"|-\n" +
			"|sv||swe||swe||[[Swedish language|Swedish]]||lang=\"sv\"|Svenska||\n" +
			"|-\n" +
			"|sw||swa||swa + [[ISO 639:swa|2]]||[[Swahili language|Swahili]]||lang=\"sw\"|Kiswahili||\n" +
			"|-\n" +
			"|ta||tam||tam||[[Tamil language|Tamil]]||lang=\"ta\"|தமிழ�?||\n" +
			"|-\n" +
			"|te||tel||tel||[[Telugu language|Telugu]]||lang=\"te\"|తెల�?గ�?||\n" +
			"|-\n" +
			"|tg||tgk||tgk||[[Tajik language|Tajik]]||{{lang|tg-Cyrl|тоҷикӣ}}; {{lang|tg-Latn|toğikī}}; {{rtl-lang|tg-Arab|‫تاجیکی}}||\n" +
			"|-\n" +
			"|th||tha||tha||[[Thai language|Thai]]||lang=\"th\"|ไทย||\n" +
			"|-\n" +
			"|ti||tir||tir||[[Tigrinya language|Tigrinya]]||lang=\"ti\"|ት�?ርኛ||\n" +
			"|-\n" +
			"|tk||tuk||tuk||[[Turkmen language|Turkmen]]||lang=\"tk\"|Türkmen; Түркмен||\n" +
			"|-\n" +
			"|tl||tgl||tgl||[[Tagalog language|Tagalog]]||lang=\"tl\"|Tagalog||\n" +
			"|-\n" +
			"|tn||tsn||tsn||[[Tswana language|Tswana]]||lang=\"tn\"|seTswana||\n" +
			"|-\n" +
			"|to||ton||ton||[[Tongan language|Tonga]]||lang=\"to\"|faka Tonga||\n" +
			"|-\n" +
			"|tr||tur||tur||[[Turkish language|Turkish]]||lang=\"tr\"|Türkçe||\n" +
			"|-\n" +
			"|ts||tso||tso||[[Tsonga language|Tsonga]]||lang=\"ts\"|xiTsonga||\n" +
			"|-\n" +
			"|tt||tat||tat||[[Tatar language|Tatar]]||{{lang|tt-Cyrl|татарча}}; {{lang|tt-Latn|tatarça}}; {{rtl-lang|tt-Arab|‫تاتارچا}}||\n" +
			"|-\n" +
			"|tw||twi||twi||[[Twi]]||lang=\"tw\"|Twi||\n" +
			"|-\n" +
			"|ty||tah||tah||[[Tahitian language|Tahitian]]||lang=\"ty\"|Reo M�?`ohi||\n" +
			"|-\n" +
			"|ug||uig||uig||[[Uyghur language|Uighur]]||{{lang|ug-Latn|Uyƣurqə}}; {{rtl-lang|ug-Arab|‫ئۇيغۇرچ }}||\n" +
			"|-\n" +
			"|uk||ukr||ukr||[[Ukrainian language|Ukrainian]]||lang=\"uk\"|Україн�?ька||\n" +
			"|-\n" +
			"|ur||urd||urd||[[Urdu]]||lang=\"ur\" dir=\"rtl\"|‫اردو||\n" +
			"|-\n" +
			"|uz||uzb||uzb + [[ISO 639:uzb|2]]||[[Uzbek language|Uzbek]]|||{{lang|uz-Latn|O'zbek}}; {{lang|uz-Cyrl|Ўзбек}}; {{rtl-lang|uz-Arab|أۇزب�?ك}}||\n" +
			"|-\n" +
			"|ve||ven||ven||[[Venda language|Venda]]||lang=\"ve\"|tshiVenḓa||\n" +
			"|-\n" +
			"|vi||vie||vie||[[Vietnamese language|Vietnamese]]||lang=\"vi\"|Tiếng Việt||\n" +
			"|-\n" +
			"|vo||vol||vol||[[Volapük]]||lang=\"vo\"|Volapük||\n" +
			"|-\n" +
			"|wa||wln||wln||[[Walloon language|Walloon]]||lang=\"wa\"|Walon||\n" +
			"|-\n" +
			"|wo||wol||wol||[[Wolof language|Wolof]]||lang=\"wo\"|Wollof||\n" +
			"|-\n" +
			"|xh||xho||xho||[[Xhosa language|Xhosa]]||lang=\"xh\"|isiXhosa||\n" +
			"|-\n" +
			"|yi||yid||yid + [[ISO 639:yid|2]]||[[Yiddish language|Yiddish]]||lang=\"yi\" dir=\"rtl\"|‫ייִדיש||\n" +
			"|-\n" +
			"|yo||yor||yor||[[Yoruba language|Yoruba]]||lang=\"yo\"|Yorùbá||\n" +
			"|-\n" +
			"|za||zha||zha + [[ISO 639:zha|2]]||[[Zhuang language|Zhuang]]||lang=\"za\"|Saɯ cueŋƅ; Saw cuengh||\n" +
			"|-\n" +
			"|zh||chi/zho||zho + [[ISO 639:zho|13]]||[[Chinese language|Chinese]]|||{{lang|zh-Hani|中文}}, {{lang|zh-Hans|汉语}}, {{lang|zh-Hant|漢語}}||\n" +
			"|-\n" +
			"|zu||zul||zul||[[Zulu language|Zulu]]||lang=\"zu\"|isiZulu||\n" +
			"|}";

		Table t = parse(content);

		System.out.println(t);
		Row row1 = t.getRows()[t.getRows().length - 1];
		assertEquals(6, row1.getCells().length);
		assertEquals("zu", row1.getCells()[0].getContent()[0].toString());
		assertEquals("zul", row1.getCells()[1].getContent()[0].toString());
		assertEquals("zul", row1.getCells()[2].getContent()[0].toString());
		assertEquals("[[Zulu language|Zulu]]", row1.getCells()[3].getContent()[0].toString());
		assertEquals("isiZulu", row1.getCells()[4].getContent()[0].toString());

		Row row2 = t.getRows()[6];

		assertEquals(6, row2.getCells().length);
		assertEquals("[[Amharic language|Amharic]]", row2.getCells()[3].getContent()[0].toString());
	}

	public void testLengthUnitsTable() throws Exception {
		String content =
			"{| class=\"wikitable\"\n" +
			"|+ [[Length]], l\n" +
			"|-----\n" +
			"!Name of unit\n" +
			"!Symbol\n" +
			"!Definition\n" +
			"!Relation to [[SI]] units\n" +
			"|-----\n" +
			"| [[ångström]] || Å\n" +
			"|\n" +
			"| ≡ 1{{e|−10}} m = 0.1 nm\n" +
			"|-----\n" +
			"| [[astronomical unit]] || AU\n" +
			"| Mean distance from Earth to the Sun\n" +
			"| = 149 597 870.691 ± 0.030 km\n" +
			"|-----\n" +
			"| [[atomic units|atomic unit of length]] || au{{Fact|date=August 2007}}\n" +
			"| ≡ a<sub>0</sub>\n" +
			"| ≈ 5.291 772 083{{e|−11}} ± 19{{e|−20}} m\n" +
			"|-----\n" +
			"| barley corn || &nbsp;\n" +
			"| ≡ 1/3 in\n" +
			"| ≈ 8.466 667 mm\n" +
			"|-----\n" +
			"| [[Bohr radius]] || a<sub>0</sub>; b\n" +
			"| ≡ [[fine structure constant|α]]/(4π[[Rydberg constant|''R''<sub>∞</sub>]])\n" +
			"| ≈ 5.291 772 083{{e|−11}} ± 19{{e|−20}} m\n" +
			"|-----\n" +
			"| cable length (Imperial) || &nbsp;\n" +
			"| ≡ 608 ft\n" +
			"| = 185.3184 m\n" +
			"|-----\n" +
			"| [[cable length]] (International) || &nbsp;\n" +
			"| ≡ 1/10 NM\n" +
			"| = 185.2 m\n" +
			"|-----\n" +
			"| cable length (U.S.) || &nbsp;\n" +
			"| ≡ 720 ft\n" +
			"| = 219.456 m\n" +
			"|-----\n" +
			"| [[calibre]] || cal\n" +
			"| ≡ 1 in\n" +
			"| = 25.4 mm\n" +
			"|-----\n" +
			"| [[chain (unit)|chain]] ([[Edmund Gunter|Gunter's]]; Surveyor's) || ch\n" +
			"| ≡ 66.0 ft (4 rods)\n" +
			"| = 20.1168 m\n" +
			"|-----\n" +
			"| [[chain (unit)|chain]] ([[Jesse Ramsden|Ramsden]]'s<!--- Ramsden: http://www.scienceandsociety.co.uk/results.asp?image=10280167&wwwflag=2&imagepos=6 Ramden: [http://aurora.rg.iupui.edu/~schadow/units/UCUM/ucum.html The Unified Code for Units of Measures] --->; Engineer's) || ch\n" +
			"| ≡ 100 ft\n" +
			"| = 30.48 m\n" +
			"|-----\n" +
			"| cubit || &nbsp;\n" +
			"| ≡ 18 in\n" +
			"| = 0.4572 m\n" +
			"|-----\n" +
			"| ell || ell\n" +
			"| ≡ 45 in\n" +
			"| = 1.143 m\n" +
			"|-----\n" +
			"|rowspan=\"2\"| [[fathom]] ||rowspan=\"2\"| fm\n" +
			"| ≡ 6 ft\n" +
			"| = 1.8288 m\n" +
			"|-\n" +
			"| ≈ 1/1000 NM\n" +
			"| = 1.852 m\n" +
			"|-----\n" +
			"| [[Femtometre|fermi]] || fm\n" +
			"| ≡ 1{{e|−15}} m\n" +
			"| = 1{{e|−15}} m\n" +
			"|-----\n" +
			"| finger || &nbsp;\n" +
			"| ≡ 7/8 in\n" +
			"| = 22.225 mm\n" +
			"|-----\n" +
			"| finger (cloth) || &nbsp;\n" +
			"| ≡ 4 ½ in\n" +
			"| = 0.1143 m\n" +
			"|-----\n" +
			"| [[foot (unit of length)|foot]] (Benoît) || ft (Ben)\n" +
			"|\n" +
			"| ≈ 0.304 799 735 m\n" +
			"|-----\n" +
			"| [[foot (unit of length)|foot]] (Clarke's; Cape) || ft (Cla)\n" +
			"|\n" +
			"| ≈ 0.304 797 265 4 m\n" +
			"|-----\n" +
			"| [[foot (unit of length)|foot]] (Indian) || ft Ind\n" +
			"|\n" +
			"| ≈ 0.304 799 514 m\n" +
			"|-----\n" +
			"| [[foot (unit of length)|foot]] (International) || ft\n" +
			"| ≡ 1/3 yd\n" +
			"| = 0.3048 m\n" +
			"|-----\n" +
			"| [[foot (unit of length)|foot]] (Sear's) || ft (Sear)\n" +
			"|\n" +
			"| ≈ 0.304 799 47 m\n" +
			"|-----\n" +
			"| [[foot (unit of length)|foot]] (U.S. Survey) || ft (US)\n" +
			"| ≡ 1200/3937 m <ref name=nbs>National Bureau of Standards. (June 30, 1959). ''Refinement of values for the yard and the pound''. Federal Register, viewed September 20, 2006 at [http://www.ngs.noaa.gov/PUBS_LIB/FedRegister/FRdoc59-5442.pdf National Geodetic Survey web site].</ref>\n" +
			"| ≈ 0.304 800 610 m\n" +
			"|-----\n" +
			"| [[furlong]] || fur\n" +
			"| ≡ 660 ft  (10 chains)\n" +
			"| = 201.168 m\n" +
			"|-----\n" +
			"| [[geographical mile]] || mi\n" +
			"| ≡ 6082 ft\n" +
			"| = 1853.7936 m\n" +
			"|-----\n" +
			"| hand || &nbsp;\n" +
			"| ≡ 4 in\n" +
			"| = 0.1016 m\n" +
			"|-----\n" +
			"| [[inch]] || in\n" +
			"| ≡ 1/36 yd\n" +
			"| = 25.4 mm\n" +
			"|-----\n" +
			"| [[league (unit)|league]] || lea\n" +
			"| ≡ 3 mi\n" +
			"| = 4828.032 m\n" +
			"|-----\n" +
			"| [[light-day]] || &nbsp;\n" +
			"| ≡ 24 light-hours\n" +
			"| = 2.590 206 837 12{{e|13}} m\n" +
			"|-----\n" +
			"| [[light-hour]] || &nbsp;\n" +
			"| ≡ 60 light-minutes\n" +
			"| = 1.079 252 848 8{{e|12}} m\n" +
			"|-----\n" +
			"| [[light-minute]] || &nbsp;\n" +
			"| ≡ 60 light-seconds\n" +
			"| = 1.798 754 748{{e|10}} m\n" +
			"|-----\n" +
			"| [[light second|light-second]] || &nbsp;\n" +
			"|\n" +
			"| ≡ 2.997 924 58{{e|8}} m\n" +
			"|-----\n" +
			"| [[light year|light-year]] || l.y.\n" +
			"| ≡ ''c''<sub>0</sub> × 86 400 × 365.25\n" +
			"| = 9.460 730 472 580 8{{e|15}} m\n" +
			"|-----\n" +
			"| line || ln\n" +
			"| ≡ 1/12 in (Klein 1988, 63)\n" +
			"| ≈ 2.116 667 mm\n" +
			"|-----\n" +
			"| link (Gunter's; Surveyor's) || lnk\n" +
			"| ≡ 1/100 ch\n" +
			"| = 0.201 168 m\n" +
			"|-----\n" +
			"| link (Ramsden's; Engineer's) || lnk\n" +
			"| ≡ 1 ft\n" +
			"| = 0.3048 m\n" +
			"|-----\n" +
			"| [[metre]] ([[SI base unit]]) || m\n" +
			"| ≡ 1 m\n" +
			"| = 1 m\n" +
			"|-----\n" +
			"| mickey || &nbsp;\n" +
			"| ≡ 1/200 in\n" +
			"| = 1.27{{e|−4}} m\n" +
			"|-----\n" +
			"| [[micrometre|micron]]|| µ\n" +
			"|\n" +
			"| ≡ 1.000{{e|−6}} m\n" +
			"|-----\n" +
			"| [[thou (unit of length)|mil]]; thou || mil\n" +
			"| ≡ 1.000{{e|−3}} in\n" +
			"| = 2.54{{e|−5}} m\n" +
			"|-----\n" +
			"| [[Norwegian/Swedish mil|mil]] (Sweden and Norway) || mil\n" +
			"| ≡ 10 km\n" +
			"| = 10000 m\n" +
			"|-----\n" +
			"| [[mile]] || mi\n" +
			"| ≡ 1760 yd = 5280 ft\n" +
			"| = 1609.344 m\n" +
			"|-----\n" +
			"| [[mile]] (U.S. Survey) || mi\n" +
			"| ≡ 5280 ft (US)\n" +
			"| = 5280 × 1200/3937 m ≈ 1609.347 219 m\n" +
			"|-----\n" +
			"| nail (cloth) || &nbsp;\n" +
			"| ≡ 2 ¼ in\n" +
			"| = 57.15 mm\n" +
			"|-----\n" +
			"| nautical league || NL; nl\n" +
			"| ≡ 3 NM\n" +
			"| = 5556 m\n" +
			"|-----\n" +
			"| [[nautical mile]] || NM; nmi\n" +
			"| ≡ 1852 m\n" +
			"| ≡ 1852 m\n" +
			"|-----\n" +
			"| [[nautical mile]] (Admiralty) || NM (Adm); nmi (Adm)\n" +
			"| ≡ 6080 ft\n" +
			"| = 1853.184 m\n" +
			"|-----\n" +
			"| pace || &nbsp;\n" +
			"| ≡ 2.5 ft\n" +
			"| = 0.762 m\n" +
			"|-----\n" +
			"| palm || &nbsp;\n" +
			"| ≡ 3 in\n" +
			"| = 76.2 mm\n" +
			"|-----\n" +
			"| [[parsec]] || pc\n" +
			"| ≈ 180 × 60 × 60/π AU<br>\n" +
			"= 206264.8062 AU<br>\n" +
			"= 3.26156378 light-years\n" +
			"| = 3.08567782 {{e|16}} ± 6{{e|6}} m <ref name=Seidelmann>P. Kenneth Seidelmann, Ed. (1992). ''Explanatory Supplement to the Astronomical Almanac.'' Sausalito, CA: University Science Books. p. 716 and s.v. parsec in Glossary.</ref>\n" +
			"|-----\n" +
			"| point ([[American Typefounders Association|ATA]]) || pt\n" +
			"| ≡ 0.013837 in\n" +
			"| = 0.351 459 8 mm\n" +
			"|-----\n" +
			"| point (Didot; European) || pt\n" +
			"|\n" +
			"| ≡ 0.376 065 mm\n" +
			"|-----\n" +
			"| point (metric) || pt\n" +
			"| ≡ 3/8 mm\n" +
			"| = 0.375 mm\n" +
			"|-----\n" +
			"| point ([[PostScript]])|| pt\n" +
			"| ≡ 1/72 in\n" +
			"| ≈ 0.352 778 mm\n" +
			"|-----\n" +
			"| quarter || &nbsp;\n" +
			"| ≡ ¼ yd\n" +
			"| = 0.2286 m\n" +
			"|-----\n" +
			"| [[rod (unit)|rod]]; pole; perch || rd\n" +
			"| ≡ 16 ½ ft\n" +
			"| = 5.0292 m\n" +
			"|-----\n" +
			"| rope || rope\n" +
			"| ≡ 20 ft\n" +
			"| = 6.096 m\n" +
			"|-----\n" +
			"| span || &nbsp;\n" +
			"| ≡ 6 in\n" +
			"| = 0.1524 m\n" +
			"|-----\n" +
			"| span (cloth) || &nbsp;\n" +
			"| ≡ 9 in\n" +
			"| = 0.2286 m\n" +
			"|-----\n" +
			"| spat[http://www.unc.edu/~rowlett/units/dictS.html] ||\n" +
			"| ≡ 10<sup>12</sup> m\n" +
			"| = 1 Tm\n" +
			"|-----\n" +
			"| stick || &nbsp;\n" +
			"| ≡ 2 in\n" +
			"| = 50.8 mm\n" +
			"|-----\n" +
			"| [[stigma (unit)|stigma]]; pm|| &nbsp;\n" +
			"| ≡ 1.000{{e|−12}} m\n" +
			"| ≡ 1.000{{e|−12}} m\n" +
			"|-----\n" +
			"| telegraph [[mile]] || mi\n" +
			"| ≡ 6087 ft\n" +
			"| = 1855.3176 m\n" +
			"|-----\n" +
			"| [[twip]] || twp\n" +
			"| ≡ 1/1440 in\n" +
			"| ≈ 1.763 889{{e|−5}} m\n" +
			"|-----\n" +
			"| [[x unit]]; [[siegbahn]] || xu\n" +
			"|\n" +
			"| ≈ 1.0021{{e|−13}} m\n" +
			"|-----\n" +
			"| [[yard]] (International) || yd\n" +
			"| ≡3 ft ≡ 0.9144 m <ref name=nbs/>\n" +
			"| = 0.9144 m\n" +
			"|}";

		Table t = parse(content);
	}

	public void testTableInternetTLDs() throws Exception {
		String content =
			"{| class=\"wikitable\"\n" +
			"|- style=\"background-color: #a0d0ff;\"\n" +
			"!iTLD!!Entity!!Notes\n" +
			"|-\n" +
			"\n" +
			"| [[.arpa]] || Address and Routing Parameter Area || This is an internet infrastructure TLD.\n" +
			"|-\n" +
			"| [[.root]] || N/A|| Diagnostic marker to indicate a root zone load was not truncated.\n" +
			"|- style=\"background-color: #a0d0ff;\"\n" +
			"![[gTLD]]!!Entity!!Notes\n" +
			"|-\n" +
			"| [[.aero]] || air-transport industry || Must verify eligibility for registration; only those in various categories of air-travel-related entities may register.\n" +
			"|-\n" +
			"| [[.asia]] || Asia-Pacific region || This is a TLD for companies, organizations, and individuals based in the region of Asia, Australia, and the Pacific.\n" +
			"|-\n" +
			"| [[.biz]] || business || This is an open TLD; any person or entity is permitted to register; however, registrations may be challenged later if they are not by commercial entities in accordance with the domain's charter.\n" +
			"|-\n" +
			"| [[.cat]] || Catalan || This is a TLD for websites in the [[Catalan language]] or related to Catalan culture.\n" +
			"|-\n" +
			"| [[.com]] || commercial || This is an open TLD; any person or entity is permitted to register.\n" +
			"|-\n" +
			"| [[.coop]] || cooperatives || The .coop TLD is limited to cooperatives as defined by the [[Rochdale Principles]].\n" +
			"|-\n" +
			"| [[.edu]] || educational || The .edu TLD is limited to institutions of learning (mostly U.S.), such as 2 and 4-year colleges and universities.\n" +
			"|-\n" +
			"| [[.gov]] || governmental || The .gov TLD is limited to U.S. governmental entities and agencies (commonly [[U.S. Federal Government | federal-level]]).\n" +
			"|-\n" +
			"| [[.info]] || information || This is an open TLD; any person or entity is permitted to register.\n" +
			"|-\n" +
			"| [[.int]] || international organizations || The .int TLD is strictly limited to organizations, offices, and programs which are endorsed by a treaty between two or more nations.\n" +
			"|-\n" +
			"| [[.jobs]] || companies || The .jobs TLD is designed to be added after the names of established companies with jobs to advertise. At this time, owners of a \"company.jobs\" domain are not permitted to post jobs of third party employers.\n" +
			"|-\n" +
			"| [[.mil]] || [[Military of the United States|United States Military]] || The .mil TLD is limited to use by the U.S. military.\n" +
			"|-\n" +
			"| [[.mobi]] || mobile devices || Must be used for mobile-compatible sites in accordance with standards.\n" +
			"|-\n" +
			"| [[.museum]] || museums || Must be verified as a legitimate museum.\n" +
			"|-\n" +
			"| [[.name]] || individuals, by name || This is an open TLD; any person or entity is permitted to register; however, registrations may be challenged later if they are not by individuals (or the owners of fictional characters) in accordance with the domain's charter\n" +
			"|-\n" +
			"| [[.net]] || network || This is an open TLD; any person or entity is permitted to register.\n" +
			"|-\n" +
			"| [[.org]] || organization || This is an open TLD; any person or entity is permitted to register.\n" +
			"|-\n" +
			"| [[.pro]] || professions || Currently, .pro is reserved for licensed doctors, attorneys, and certified public accountants only. A professional seeking to register a .pro domain must provide their registrar with the appropriate credentials.\n" +
			"|-\n" +
			"| [[.tel]] || Internet communication services ||\n" +
			"|-\n" +
			"| [[.travel]] || travel and travel-agency related sites || Must be verified as a legitimate travel-related entity.\n" +
			"|-\n" +
			"<!--\n" +
			"|-\n" +
			"| [[.xxx]] || [[Pornography|Pornographic]] websites || &nbsp;\n" +
			"ICANN has approved this TLD in principle, but it has not been added to the root yet.\n" +
			"-->\n" +
			"|- style=\"background-color: #a0d0ff;\"\n" +
			"![[ccTLD]]!!Country/dependency/region!!Notes\n" +
			"|-\n" +
			"| [[.ac]] || [[Ascension Island]] || &nbsp;\n" +
			"|-\n" +
			"| [[.ad]] || [[Andorra]] || &nbsp;\n" +
			"|-\n" +
			"| [[.ae]] || [[United Arab Emirates]] || &nbsp;\n" +
			"|-\n" +
			"| [[.af]] || [[Afghanistan]] || &nbsp;\n" +
			"|-\n" +
			"| [[.ag]] || [[Antigua and Barbuda]] || &nbsp;\n" +
			"|-\n" +
			"| [[.ai]] || [[Anguilla]] || &nbsp;\n" +
			"|-\n" +
			"| [[.al]] || [[Albania]] || &nbsp;\n" +
			"|-\n" +
			"| [[.am]] || [[Armenia]] || &nbsp;\n" +
			"|-\n" +
			"| [[.an]] || [[Netherlands Antilles]] || &nbsp;\n" +
			"|-\n" +
			"| [[.ao]] || [[Angola]] || &nbsp;\n" +
			"|-\n" +
			"| [[.aq]] || [[Antarctica]] || Defined as per the [[Antarctic Treaty System|Antarctic Treaty]] as everything south of latitude 60°S\n" +
			"|-\n" +
			"| [[.ar]] || [[Argentina]] || &nbsp;\n" +
			"|-\n" +
			"| [[.as]] || [[American Samoa]] || &nbsp;\n" +
			"|-\n" +
			"| [[.at]] || [[Austria]] || &nbsp;\n" +
			"|-\n" +
			"| [[.au]] || [[Australia]] || Includes [[Ashmore and Cartier Islands]] and [[Coral Sea Islands]]\n" +
			"|-\n" +
			"| [[.aw]] || [[Aruba]] || &nbsp;\n" +
			"|-\n" +
			"| [[.ax]] || [[Åland]] || &nbsp;\n" +
			"|-\n" +
			"| [[.az]] || [[Azerbaijan]] || &nbsp;\n" +
			"|-\n" +
			"| [[.ba]] || [[Bosnia and Herzegovina]] || &nbsp;\n" +
			"|-\n" +
			"| [[.bb]] || [[Barbados]] || &nbsp;\n" +
			"|-\n" +
			"| [[.bd]] || [[Bangladesh]] || &nbsp;\n" +
			"|-\n" +
			"| [[.be]] || [[Belgium]] || &nbsp;\n" +
			"|-\n" +
			"| [[.bf]] || [[Burkina Faso]] || &nbsp;\n" +
			"|-\n" +
			"| [[.bg]] || [[Bulgaria]] || &nbsp;\n" +
			"|-\n" +
			"| [[.bh]] || [[Bahrain]] || &nbsp;\n" +
			"|-\n" +
			"| [[.bi]] || [[Burundi]] || &nbsp;\n" +
			"|-\n" +
			"| [[.bj]] || [[Benin]] || &nbsp;\n" +
			"|-\n" +
			"| [[.bm]] || [[Bermuda]] || &nbsp;\n" +
			"|-\n" +
			"| [[.bn]] || [[Brunei|Brunei Darussalam]] || &nbsp;\n" +
			"|-\n" +
			"| [[.bo]] || [[Bolivia]] || &nbsp;\n" +
			"|-\n" +
			"| [[.br]] || [[Brazil]] || &nbsp;\n" +
			"|-\n" +
			"| [[.bs]] || [[Bahamas]] || &nbsp;\n" +
			"|-\n" +
			"| [[.bt]] || [[Bhutan]] || &nbsp;\n" +
			"|-\n" +
			"| [[.bv]] || [[Bouvet Island]] || Not in use (Norwegian dependency; see .no)\n" +
			"|-\n" +
			"| [[.bw]] || [[Botswana]] || &nbsp;\n" +
			"|-\n" +
			"| [[.by]] || [[Belarus]] || &nbsp;\n" +
			"|-\n" +
			"| [[.bz]] || [[Belize]] || &nbsp;\n" +
			"|-\n" +
			"| [[.ca]] || [[Canada]] || Subject to Canadian Presence Requirements, see [[.ca]]\n" +
			"|-\n" +
			"| [[.cc]] || [[Cocos Islands|Cocos (Keeling) Islands]] || &nbsp;\n" +
			"|-\n" +
			"| [[.cd]] || [[Democratic Republic of the Congo]] || Formerly [[Zaire]]\n" +
			"|-\n" +
			"| [[.cf]] || [[Central African Republic]] || &nbsp;\n" +
			"|-\n" +
			"| [[.cg]] || [[Republic of the Congo]] || &nbsp;\n" +
			"|-\n" +
			"| [[.ch]] || [[Switzerland]] (Confoederatio Helvetica) || &nbsp;\n" +
			"|-\n" +
			"| [[.ci]] || [[Côte d'Ivoire]] || &nbsp;\n" +
			"|-\n" +
			"| [[.ck]] || [[Cook Islands]] || &nbsp;\n" +
			"|-\n" +
			"| [[.cl]] || [[Chile]] || &nbsp;\n" +
			"|-\n" +
			"| [[.cm]] || [[Cameroon]] || &nbsp;\n" +
			"|-\n" +
			"| [[.cn]] || [[People's Republic of China|China, mainland]] || [[Mainland China]] only: [[Hong Kong]], [[Macau]] and [[Taiwan]] use separate TLDs.\n" +
			"|-\n" +
			"| [[.co]] || [[Colombia]] || &nbsp;\n" +
			"|-\n" +
			"| [[.cr]] || [[Costa Rica]] || &nbsp;\n" +
			"|-\n" +
			"| [[.cu]] || [[Cuba]] || &nbsp;\n" +
			"|-\n" +
			"| [[.cv]] || [[Cape Verde]] || &nbsp;\n" +
			"|-\n" +
			"| [[.cx]] || [[Christmas Island]] || &nbsp;\n" +
			"|-\n" +
			"| [[.cy]] || [[Cyprus]] || &nbsp;\n" +
			"|-\n" +
			"| [[.cz]] || [[Czech Republic]] || &nbsp;\n" +
			"|-\n" +
			"| [[.de]] || [[Germany]] (Deutschland) || &nbsp;\n" +
			"|-\n" +
			"| [[.dj]] || [[Djibouti]] || &nbsp;\n" +
			"|-\n" +
			"| [[.dk]] || [[Denmark]] || &nbsp;\n" +
			"|-\n" +
			"| [[.dm]] || [[Dominica]] || &nbsp;\n" +
			"|-\n" +
			"| [[.do]] || [[Dominican Republic]] || &nbsp;\n" +
			"|-\n" +
			"| [[.dz]] || [[Algeria]] (Dzayer) || Not available for private use\n" +
			"|-\n" +
			"| [[.ec]] || [[Ecuador]] || &nbsp;\n" +
			"|-\n" +
			"| [[.ee]] || [[Estonia]] || &nbsp;\n" +
			"|-\n" +
			"| [[.eg]] || [[Egypt]] || &nbsp;\n" +
			"<!--\n" +
			"|-\n" +
			".eh is reserved for Western Sahara, but does not exist in the root\n" +
			"| [[.eh]] || [[Western Sahara]] || &nbsp;\n" +
			"-->\n" +
			"|-\n" +
			"| [[.er]] || [[Eritrea]] || &nbsp;\n" +
			"|-\n" +
			"| [[.es]] || [[Spain]] (España) || &nbsp;\n" +
			"|-\n" +
			"| [[.et]] || [[Ethiopia]] || &nbsp;\n" +
			"|-\n" +
			"| [[.eu]] || [[European Union]] || Restricted to companies and individuals in the European Union\n" +
			"|-\n" +
			"| [[.fi]] || [[Finland]] || &nbsp;\n" +
			"|-\n" +
			"| [[.fj]] || [[Fiji]] || &nbsp;\n" +
			"|-\n" +
			"| [[.fk]] || [[Falkland Islands]] || &nbsp;\n" +
			"|-\n" +
			"| [[.fm]] || [[Federated States of Micronesia]] || Used for some radio related websites outside Micronesia\n" +
			"|-\n" +
			"| [[.fo]] || [[Faroe Islands]] || &nbsp;\n" +
			"|-\n" +
			"| [[.fr]] || [[France]] || Can only be used by organisations or persons with a presence in France.\n" +
			"|-\n" +
			"| [[.ga]] || [[Gabon]] || &nbsp;\n" +
			"|-\n" +
			"| [[.gb]] || [[United Kingdom]] || Seldom used; the primary ccTLD used is [[.uk]] for [[United Kingdom]]\n" +
			"|-\n" +
			"| [[.gd]] || [[Grenada]] || &nbsp;\n" +
			"|-\n" +
			"| [[.ge]] || [[Georgia (country)|Georgia]] || &nbsp;\n" +
			"|-\n" +
			"| [[.gf]] || [[French Guiana]] || &nbsp;\n" +
			"|-\n" +
			"| [[.gg]] || [[Guernsey]] || &nbsp;\n" +
			"|-\n" +
			"| [[.gh]] || [[Ghana]] || &nbsp;\n" +
			"|-\n" +
			"| [[.gi]] || [[Gibraltar]] || &nbsp;\n" +
			"|-\n" +
			"| [[.gl]] || [[Greenland]] || &nbsp;\n" +
			"|-\n" +
			"| [[.gm]] || [[The Gambia]] || &nbsp;\n" +
			"|-\n" +
			"| [[.gn]] || [[Guinea]] || &nbsp;\n" +
			"|-\n" +
			"| [[.gp]] || [[Guadeloupe]] || &nbsp;\n" +
			"|-\n" +
			"| [[.gq]] || [[Equatorial Guinea]] || &nbsp;\n" +
			"|-\n" +
			"| [[.gr]] || [[Greece]] || &nbsp;\n" +
			"|-\n" +
			"| [[.gs]] || [[South Georgia and the South Sandwich Islands]] || &nbsp;\n" +
			"|-\n" +
			"| [[.gt]] || [[Guatemala]] || &nbsp;\n" +
			"|-\n" +
			"| [[.gu]] || [[Guam]] || &nbsp;\n" +
			"|-\n" +
			"| [[.gw]] || [[Guinea-Bissau]] || &nbsp;\n" +
			"|-\n" +
			"| [[.gy]] || [[Guyana]] || &nbsp;\n" +
			"|-\n" +
			"| [[.hk]] || [[Hong Kong]] || [[Special administrative region]] of the [[People's Republic of China]].\n" +
			"|-\n" +
			"| [[.hm]] || [[Heard Island and McDonald Islands]] || &nbsp;\n" +
			"|-\n" +
			"| [[.hn]] || [[Honduras]] || &nbsp;\n" +
			"|-\n" +
			"| [[.hr]] || [[Croatia]] (Hrvatska) || &nbsp;\n" +
			"|-\n" +
			"| [[.ht]] || [[Haiti]] || &nbsp;\n" +
			"|-\n" +
			"| [[.hu]] || [[Hungary]] || &nbsp;\n" +
			"|-\n" +
			"| [[.id]] || [[Indonesia]] || &nbsp;\n" +
			"|-\n" +
			"| [[.ie]] || [[Republic of Ireland|Ireland]] (Éire)|| &nbsp;\n" +
			"|-\n" +
			"| [[.il]] || [[Israel]] || &nbsp;\n" +
			"|-\n" +
			"| [[.im]] || [[Isle of Man]] || &nbsp;\n" +
			"|-\n" +
			"| [[.in]] || [[India]] || Under [[INRegistry]] since April 2005 except: gov.in, mil.in, ac.in, edu.in, res.in\n" +
			"|-\n" +
			"| [[.io]] || [[British Indian Ocean Territory]] || &nbsp;\n" +
			"|-\n" +
			"| [[.iq]] || [[Iraq]] || &nbsp;\n" +
			"|-\n" +
			"| [[.ir]] || [[Iran]] || &nbsp;\n" +
			"|-\n" +
			"| [[.is]] || [[Iceland]] (�?sland) || &nbsp;\n" +
			"|-\n" +
			"| [[.it]] || [[Italy]] || Restricted to companies and individuals in the [[European Union]]\n" +
			"|-\n" +
			"| [[.je]] || [[Jersey]] || &nbsp;\n" +
			"|-\n" +
			"| [[.jm]] || [[Jamaica]] || &nbsp;\n" +
			"|-\n" +
			"| [[.jo]] || [[Jordan]] || &nbsp;\n" +
			"|-\n" +
			"| [[.jp]] || [[Japan]] || &nbsp;\n" +
			"|-\n" +
			"| [[.ke]] || [[Kenya]] || &nbsp;\n" +
			"|-\n" +
			"| [[.kg]] || [[Kyrgyzstan]] || &nbsp;\n" +
			"|-\n" +
			"| [[.kh]] || [[Cambodia]] (Khmer) || &nbsp;\n" +
			"|-\n" +
			"| [[.ki]] || [[Kiribati]] || &nbsp;\n" +
			"|-\n" +
			"| [[.km]] || [[Comoros]] || &nbsp;\n" +
			"|-\n" +
			"| [[.kn]] || [[Saint Kitts and Nevis]] || &nbsp;\n" +
			"|-\n" +
			"| [[.kp]] || [[North Korea]] || &nbsp;\n" +
			"|-\n" +
			"| [[.kr]] || [[South Korea]] || &nbsp;\n" +
			"|-\n" +
			"| [[.kw]] || [[Kuwait]] || &nbsp;\n" +
			"|-\n" +
			"| [[.ky]] || [[Cayman Islands]] || &nbsp;\n" +
			"|-\n" +
			"| [[.kz]] || [[Kazakhstan]] || &nbsp;\n" +
			"|-\n" +
			"| [[.la]] || [[Laos]] || Currently being marketed as the official domain for [[Los Angeles]].\n" +
			"|-\n" +
			"| [[.lb]] || [[Lebanon]] || &nbsp;\n" +
			"|-\n" +
			"| [[.lc]] || [[Saint Lucia]] || &nbsp;\n" +
			"|-\n" +
			"| [[.li]] || [[Liechtenstein]] || &nbsp;\n" +
			"|-\n" +
			"| [[.lk]] || [[Sri Lanka]] || &nbsp;\n" +
			"|-\n" +
			"| [[.lr]] || [[Liberia]] || &nbsp;\n" +
			"|-\n" +
			"| [[.ls]] || [[Lesotho]] || &nbsp;\n" +
			"|-\n" +
			"| [[.lt]] || [[Lithuania]] || &nbsp;\n" +
			"|-\n" +
			"| [[.lu]] || [[Luxembourg]] || &nbsp;\n" +
			"|-\n" +
			"| [[.lv]] || [[Latvia]] || &nbsp;\n" +
			"|-\n" +
			"| [[.ly]] || [[Libya]] || &nbsp;\n" +
			"|-\n" +
			"| [[.ma]] || [[Morocco]] || &nbsp;\n" +
			"|-\n" +
			"| [[.mc]] || [[Monaco]] || &nbsp;\n" +
			"|-\n" +
			"| [[.md]] || [[Moldova]] || &nbsp;\n" +
			"|-\n" +
			"| [[.me]] || [[Montenegro]] || &nbsp;\n" +
			"|-\n" +
			"| [[.mg]] || [[Madagascar]] || &nbsp;\n" +
			"|-\n" +
			"| [[.mh]] || [[Marshall Islands]] || &nbsp;\n" +
			"|-\n" +
			"| [[.mk]] || [[Republic of Macedonia]] || &nbsp;\n" +
			"|-\n" +
			"| [[.ml]] || [[Mali]] || &nbsp;\n" +
			"|-\n" +
			"| [[.mm]] || [[Myanmar]] || &nbsp;\n" +
			"|-\n" +
			"| [[.mn]] || [[Mongolia]] || &nbsp;\n" +
			"|-\n" +
			"| [[.mo]] || [[Macau]] || [[Special administrative region]] of the [[People's Republic of China]].\n" +
			"|-\n" +
			"| [[.mp]] || [[Northern Mariana Islands]] || &nbsp;\n" +
			"|-\n" +
			"| [[.mq]] || [[Martinique]] || &nbsp;\n" +
			"|-\n" +
			"| [[.mr]] || [[Mauritania]] || &nbsp;\n" +
			"|-\n" +
			"| [[.ms]] || [[Montserrat]] || &nbsp;\n" +
			"|-\n" +
			"| [[.mt]] || [[Malta]] || &nbsp;\n" +
			"|-\n" +
			"| [[.mu]] || [[Mauritius]] || &nbsp;\n" +
			"|-\n" +
			"| [[.mv]] || [[Maldives]] || &nbsp;\n" +
			"|-\n" +
			"| [[.mw]] || [[Malawi]] || &nbsp;\n" +
			"|-\n" +
			"| [[.mx]] || [[Mexico]] || &nbsp;\n" +
			"|-\n" +
			"| [[.my]] || [[Malaysia]] || Must be registered with a company in [[Malaysia]] to register. Currently famous for the literal word '[[my]]'.\n" +
			"|-\n" +
			"| [[.mz]] || [[Mozambique]] || &nbsp;\n" +
			"|-\n" +
			"| [[.na]] || [[Namibia]] || &nbsp;\n" +
			"|-\n" +
			"| [[.nc]] || [[New Caledonia]] || &nbsp;\n" +
			"|-\n" +
			"| [[.ne]] || [[Niger]] || &nbsp;\n" +
			"|-\n" +
			"| [[.nf]] || [[Norfolk Island]] || &nbsp;\n" +
			"|-\n" +
			"| [[.ng]] || [[Nigeria]] || &nbsp;\n" +
			"|-\n" +
			"| [[.ni]] || [[Nicaragua]] || &nbsp;\n" +
			"|-\n" +
			"| [[.nl]] || [[Netherlands]] || &nbsp;\n" +
			"|-\n" +
			"| [[.no]] || [[Norway]] || Must be registered with a company in [[Norway]] to register.\n" +
			"|-\n" +
			"| [[.np]] || [[Nepal]] || &nbsp;\n" +
			"|-\n" +
			"| [[.nr]] || [[Nauru]] || &nbsp;\n" +
			"|-\n" +
			"| [[.nu]] || [[Niue]] || Commonly used for Scandinavian and Dutch websites, because in those languages 'nu' means 'now'.\n" +
			"|-\n" +
			"| [[.nz]] || [[New Zealand]] || &nbsp;\n" +
			"|-\n" +
			"| [[.om]] || [[Oman]] || &nbsp;\n" +
			"|-\n" +
			"| [[.pa]] || [[Panama]] || &nbsp;\n" +
			"|-\n" +
			"| [[.pe]] || [[Peru]] || &nbsp;\n" +
			"|-\n" +
			"| [[.pf]] || [[French Polynesia]] || With [[Clipperton Island]]\n" +
			"|-\n" +
			"| [[.pg]] || [[Papua New Guinea]] || &nbsp;\n" +
			"|-\n" +
			"| [[.ph]] || [[Philippines]] || &nbsp;\n" +
			"|-\n" +
			"| [[.pk]] || [[Pakistan]] || &nbsp;\n" +
			"|-\n" +
			"| [[.pl]] || [[Poland]] || &nbsp;\n" +
			"|-\n" +
			"| [[.pm]] || [[Saint-Pierre and Miquelon]] || &nbsp;\n" +
			"|-\n" +
			"| [[.pn]] || [[Pitcairn Islands]] || &nbsp;\n" +
			"|-\n" +
			"| [[.pr]] || [[Puerto Rico]] || &nbsp;\n" +
			"|-\n" +
			"| [[.ps]] || [[Palestinian territories]] || PA-controlled [[West Bank]] and [[Gaza Strip]]\n" +
			"|-\n" +
			"| [[.pt]] || [[Portugal]] || Only available for Portuguese registered brands and companies\n" +
			"|-\n" +
			"| [[.pw]] || [[Palau]] || &nbsp;\n" +
			"|-\n" +
			"| [[.py]] || [[Paraguay]] || &nbsp;\n" +
			"|-\n" +
			"| [[.qa]] || [[Qatar]] || &nbsp;\n" +
			"|-\n" +
			"| [[.re]] || [[Réunion]] || &nbsp;\n" +
			"|-\n" +
			"| [[.ro]] || [[Romania]] || &nbsp;\n" +
			"|-\n" +
			"| [[.rs]] || [[Serbia]] || &nbsp;\n" +
			"|-\n" +
			"| [[.ru]] || [[Russia]] || &nbsp;\n" +
			"|-\n" +
			"| [[.rw]] || [[Rwanda]] || &nbsp;\n" +
			"|-\n" +
			"| [[.sa]] || [[Saudi Arabia]] || &nbsp;\n" +
			"|-\n" +
			"| [[.sb]] || [[Solomon Islands]] || &nbsp;\n" +
			"|-\n" +
			"| [[.sc]] || [[Seychelles]] || &nbsp;\n" +
			"|-\n" +
			"| [[.sd]] || [[Sudan]] || &nbsp;\n" +
			"|-\n" +
			"| [[.se]] || [[Sweden]] || &nbsp;\n" +
			"|-\n" +
			"| [[.sg]] || [[Singapore]] || &nbsp;\n" +
			"|-\n" +
			"| [[.sh]] || [[Saint Helena]] || &nbsp;\n" +
			"|-\n" +
			"| [[.si]] || [[Slovenia]] || &nbsp;\n" +
			"|-\n" +
			"| [[.sj]] || [[Svalbard]] and [[Jan Mayen]] Islands || Not in use (Norwegian dependencies; see .no)\n" +
			"|-\n" +
			"| [[.sk]] || [[Slovakia]] || &nbsp;\n" +
			"|-\n" +
			"| [[.sl]] || [[Sierra Leone]] || &nbsp;\n" +
			"|-\n" +
			"| [[.sm]] || [[San Marino]] || &nbsp;\n" +
			"|-\n" +
			"| [[.sn]] || [[Senegal]] || &nbsp;\n" +
			"|-\n" +
			"| [[.so (domain name)|.so]] || [[Somalia]] || &nbsp;\n" +
			"|-\n" +
			"| [[.sr]] || [[Suriname]] || &nbsp;\n" +
			"|-\n" +
			"| [[.st]] || [[São Tomé and Príncipe]] || &nbsp;\n" +
			"|-\n" +
			"| [[.su]] || former [[Soviet Union]] || Still in use\n" +
			"|-\n" +
			"| [[.sv]] || [[El Salvador]] || &nbsp;\n" +
			"|-\n" +
			"| [[.sy]] || [[Syria]] || &nbsp;\n" +
			"|-\n" +
			"| [[.sz]] || [[Swaziland]] || &nbsp;\n" +
			"|-\n" +
			"| [[.tc]] || [[Turks and Caicos Islands]] || &nbsp;\n" +
			"|-\n" +
			"| [[.td]] || [[Chad]] || &nbsp;\n" +
			"|-\n" +
			"| [[.tf]] || [[French Southern and Antarctic Lands]] || &nbsp;\n" +
			"|-\n" +
			"| [[.tg]] || [[Togo]] || &nbsp;\n" +
			"|-\n" +
			"| [[.th]] || [[Thailand]] || &nbsp;\n" +
			"|-\n" +
			"| [[.tj]] || [[Tajikistan]] || &nbsp;\n" +
			"|-\n" +
			"| [[.tk]] || [[Tokelau]] || Also used as a free domain service to the public\n" +
			"|-\n" +
			"| [[.tl]] || [[East Timor]] || Old code .tp is still in use\n" +
			"|-\n" +
			"| [[.tm]] || [[Turkmenistan]] || &nbsp;\n" +
			"|-\n" +
			"| [[.tn]] || [[Tunisia]] || &nbsp;\n" +
			"|-\n" +
			"| [[.to]] || [[Tonga]] || &nbsp;\n" +
			"|-\n" +
			"| [[.tp]] || [[East Timor]] || ISO code has changed to TL; .tl is now assigned but .tp is still in use\n" +
			"|-\n" +
			"| [[.tr]] || [[Turkey]] || &nbsp;\n" +
			"|-\n" +
			"| [[.tt]] || [[Trinidad and Tobago]] || &nbsp;\n" +
			"|-\n" +
			"| [[.tv]] || [[Tuvalu]] ||Much used by television broadcasters. Also sold as advertising domains\n" +
			"|-\n" +
			"| [[.tw]] || [[Taiwan]], [[Republic of China]] || Used in the [[Republic of China]], namely [[Taiwan]], [[Penghu]], [[Kinmen]], and [[Matsu Islands|Matsu]].\n" +
			"|-\n" +
			"| [[.tz]] || [[Tanzania]] || &nbsp;\n" +
			"|-\n" +
			"| [[.ua]] || [[Ukraine]] || &nbsp;\n" +
			"|-\n" +
			"| [[.ug]] || [[Uganda]] || &nbsp;\n" +
			"|-\n" +
			"| [[.uk]] || [[United Kingdom]] || &nbsp;\n" +
			"|-\n" +
			"| [[.um]] || [[United States Minor Outlying Islands]] || &nbsp;\n" +
			"|-\n" +
			"| [[.us]] || [[United States|United States of America]] || Commonly used by [[List_of_U.S._state_legislatures#External links | U.S. State]] and [[Local government in the United States|local governments]] instead of .gov TLD || &nbsp;\n" +
			"|-\n" +
			"| [[.uy]] || [[Uruguay]] || &nbsp;\n" +
			"|-\n" +
			"| [[.uz]] || [[Uzbekistan]] || &nbsp;\n" +
			"|-\n" +
			"| [[.va]] || [[Vatican City|Vatican City State]] || &nbsp;\n" +
			"|-\n" +
			"| [[.vc]] || [[Saint Vincent and the Grenadines]] || &nbsp;\n" +
			"|-\n" +
			"| [[.ve]] || [[Venezuela]] || &nbsp;\n" +
			"|-\n" +
			"| [[.vg]] || [[British Virgin Islands]] || &nbsp;\n" +
			"|-\n" +
			"| [[.vi]] || [[U.S. Virgin Islands]] || &nbsp;\n" +
			"|-\n" +
			"| [[.vn]] || [[Vietnam]] || &nbsp;\n" +
			"|-\n" +
			"| [[.vu]] || [[Vanuatu]] || &nbsp;\n" +
			"|-\n" +
			"| [[.wf]] || [[Wallis and Futuna]] || &nbsp;\n" +
			"|-\n" +
			"| [[.ws]] || [[Samoa]] || Formerly Western Samoa\n" +
			"|-\n" +
			"| [[.ye]] || [[Yemen]] || &nbsp;\n" +
			"|-\n" +
			"| [[.yt]] || [[Mayotte]] || &nbsp;\n" +
			"|-\n" +
			"| [[.yu]] || [[Federal Republic of Yugoslavia|Yugoslavia]] || Now used for [[Serbia]] and [[Montenegro]]\n" +
			"|-\n" +
			"| [[.za]] || [[South Africa]] (Zuid-Afrika) || &nbsp;\n" +
			"|-\n" +
			"| [[.zm]] || [[Zambia]] || &nbsp;\n" +
			"|-\n" +
			"| [[.zw]] || [[Zimbabwe]] || &nbsp;\n" +
			"|- style=\"background-color: #a0d0ff;\"\n" +
			"![[IDNA]] TLD<ref>The IDNA TLDs have been added for the purpose of testing the use of IDNA at the top level, and are likely to be temporary. Each of the eleven TLDs encodes a word meaning \"test\" in some language. See the [http://www.icann.org/announcements/announcement-15oct07.htm ICANN announcement of 15 October 2007] and the [http://idn.icann.org/ IDN TLD evaluation gateway].</ref>!!Language!!Word\n" +
			"|-\n" +
			"| .xn--0zwm56d || [[simplified Chinese]] || 测试\n" +
			"|-\n" +
			"| .xn--11b5bs3a9aj6g || [[Hindi]] || परीक�?षा\n" +
			"|-\n" +
			"| .xn--80akhbyknj4f || [[Russian language|Russian]] || и�?пытание\n" +
			"|-\n" +
			"| .xn--9t4b11yi5a || [[Korean language|Korean]] || 테스트\n" +
			"|-\n" +
			"| .xn--deba0ad || [[Yiddish]] || טעסט\n" +
			"|-\n" +
			"| .xn--g6w251d || [[traditional Chinese]] || 測試\n" +
			"|-\n" +
			"| .xn--hgbk6aj7f53bba || [[Persian language|Persian]] || آزمایشی\n" +
			"|-\n" +
			"| .xn--hlcj6aya9esc7a || [[Tamil language|Tamil]] || பரிட�?சை\n" +
			"|-\n" +
			"| .xn--jxalpdlp || [[Greek language|Greek]] || δοκιμή\n" +
			"|-\n" +
			"| .xn--kgbechtv || [[Arabic language|Arabic]] || إختبار\n" +
			"|-\n" +
			"| .xn--zckzah || [[Japanese language|Japanese]] || テスト\n" +
			"|}";

		Table t = parse(content);
	}
}
