all: slidy

revealjs:
	pandoc --to=revealjs --standalone lyah.md --output=lyah.html

slidy:
	pandoc --to=slidy --standalone lyah.md | ./point-to-local-slidy.sh > lyah.html
