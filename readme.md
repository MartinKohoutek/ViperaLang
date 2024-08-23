# Lang

Cílem je vytvořit interpreter jazyka Python. Jako výchozí materiál jsem použil publikaci [Crafting Interpreters](https://craftinginterpreters.com/contents.html), která popisuje, jak vytvořit Interpreter pro fiktivní jazyk Lox.

Interpreter jazyka Python bude nejprve vytvoren v jazyce Java, poté bude převeden do C/C++ a rozšířen o některé pokročilejší funkce - např. Garbage Collection.

## Základní funkce jazyka

- Datové typy: int, float, bool, str, None
- Bloky: řešené pomocí odsazování (INDENT, DEDENT)
- Komentáře: #
- Proměnné: x = 1, name = "Jan"
- Aritmetické operátory: 
	1) binarni: +, -, *, /, 
	2) unarni: -
- Logicke operatory: and, or, not
- Porovnavani: ==, !=, >, <, >=, <=
- Rizeni behu programu:
	1) if E: S elif E: S else S:
	2) for i in range(5): S
	3) while E: S
- Funkce: 
	def f(param): 
		return f"{param}"
- Třídy: 

	class Person:

		def __init__(self, name):
			self.name = name
			
		def greet(self):
			returf f"{self.name}"

## Možné rozšíření jazyka

- Datové typy: Decimal, complex, list, tuple, range, dict, set, file
- Aritmetické operátory: //, **, %
- Cyklus: match-case

## Otevřené otázky
	1) Implementovat retezce s '', '''?
	2) Implementovat komentáře """?
	3) Implementovat += apod?
	4) Resit logicke operatory se zavorkou i bez? (5 > 3) or (2 < 3)
	5) Resit retezeni logickych operatoru? a < b < c?
	6) Resit porovnavani retezcu, [], ()?
	7) Cykly - resit break, continue?
	8) Vyjímky
 
## Nebude implementováno
- podpora modulů
- lamda funkce
