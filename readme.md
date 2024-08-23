# Lang

Cílem je vytvořit interpreter jazyka Python. Jako výchozí materiál jsem použil publikaci [Crafting Interpreters](https://craftinginterpreters.com/contents.html), která popisuje, jak vytvořit Interpreter pro fiktivní jazyk Lox.

Interpreter jazyka Python bude nejprve vytvoren v jazyce Java, poté bude převeden do C/C++ a rozšířen o některé pokročilejší funkce - např. Garbage Collection.

Použité technologie: Java Zulu SDK, v. 21

## Základní funkce jazyka (1.fáze vývoje)
- Datové typy: int, str, list
- Bloky: řešené pomocí odsazování (INDENT, DEDENT)
- Komentáře: #
- Aritmetické operátory (celociselne): 
	1) binarni: +, -, *, //, 
	2) unarni: -
	3) scitani retezcu
- Rizeni behu programu:
	1) if E: S elif E: S else S:
	2) while E: S

## Další fáze vývoje 
- Datové typy: float, bool, None, tuple, range, dict, set
- Proměnné: x = 1, name = "Jan"
- Aritmetické operátory (float): 
	1) binarni: +, -, *, /, 
	2) unarni: -
- Logicke operatory: and, or, not
- Porovnavani: ==, !=, >, <, >=, <=
	- a) vyresit porovnavani retezcu, listu, tuplu atd
- Rizeni behu programu:
	1) for i in range(5): S
- Cykly - pridat break, continue?
- Vyjímky

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

- Datové typy: Decimal, complex, file
- Aritmetické operátory: **, %
- Cyklus: match-case
- Implementovat += atd.

## Otevřené otázky
	1) Implementovat retezce s '', '''?
	2) Implementovat komentáře """?
 
## Nebude implementováno
- podpora modulů
- lamda funkce
- nebude řešeno retezeni logickych operatoru: a < b < c 
