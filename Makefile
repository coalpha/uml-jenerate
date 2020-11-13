IN_DIR := src/java
OUT_DIR := bin/java
JAR_DIR := dist
LIBS := opre-v1.0.0.jar
LIBS_PATH := lib
LIBS_JOINED := $(foreach lib,$(LIBS),;$(LIBS_PATH)/$(lib))
NAME := uml-jenerate-v0.0.1

src_files = $(wildcard $(IN_DIR)/*.java)
src_names = $(src_files:$(IN_DIR)/%.java=%)
artifacts = $(src_names:%=$(OUT_DIR)/%.class)

JAVAC_ARGS = -cp "$(IN_DIR);$(LIBS_JOINED)" -d $(OUT_DIR) -g -parameters
JAVA_ARGS  = -cp "$(OUT_DIR);$(LIBS_JOINED)"
RUN_ARGS   = . UML.dot UML.png

default:
	@echo src_files = $(src_files)
	@echo Please choose a target

jar: $(JAR_DIR)/$(NAME).jar $(foreach lib,$(LIBS),$(JAR_DIR)/$(lib))
	

$(OUT_DIR)/%.class: $(IN_DIR)/%.java
	@echo ----- MAK $< -----
	@-javac $(JAVAC_ARGS) $<

$(JAR_DIR):
	@mkdir $@

$(JAR_DIR)/$(NAME).jar: dist $(artifacts)
	jar cvfm $@ Manifest.txt -C $(OUT_DIR) .

$(JAR_DIR)/%.jar:
	$(eval L=$(patsubst $(JAR_DIR)/%,%,$@))
	copy $(LIBS_PATH)\$(L) $(JAR_DIR)\$(L)

run~%: $(artifacts)
	$(eval J=$(notdir $(patsubst run~%,%,$@)))
	@echo ----- RUN $(J) -----
	@java $(JAVA_ARGS) $(J) $(RUN_ARGS)
	@echo ----- END $(J) -----

clean:
	-rd /s /q "$(OUT_DIR)"

.PHONY: default jar run~% noop~%
.SECONDARY:
