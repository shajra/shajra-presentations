TARGET_DIR := target/scala-2.11/tut
HEADER := $(TARGET_DIR)/header.tex
SOURCES := $(sort $(wildcard $(TARGET_DIR)/*.md))
PDF := $(TARGET_DIR)/scala-validation.pdf


pdf: $(PDF)

$(PDF): $(HEADER) $(SOURCES)
	pandoc \
		--to beamer \
		-H $(HEADER) \
		-V theme:Boadilla \
		--listings \
		--slide-level 2 \
		--output $(PDF) \
                $(SOURCES)

clean:
	rm -f $(PDF)

loop:
	iwatch \
		-r \
		-e close_write \
		-t '.+\.md|.+\.tex|Makefile' \
		-x src \
		-c make \
		.

.PHONY: pdf clean loop
