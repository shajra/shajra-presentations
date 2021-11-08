TEXINPUTS := ./style/:$(TEXINPUTS)
export TEXINPUTS


pdf:
	llmk

clean::
	llmk -c -C
	find . -name '*-converted-to.pdf' -exec rm {} +
	find . -name 'indent.log' -exec rm {} +
	rm -rf \
		haskell-simspace.nav \
		haskell-simspace.snm \
		haskell-simspace.vrb \
		_minted-haskell-simspace \

.PHONY: pdf watch clean
