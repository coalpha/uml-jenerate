src  := src
lib  := lib
obj  := bin
dist := dist

name := uml-jenerate-v0.0.3

out := $(obj)/$(name)
jar := $(name).jar

classpathify = $(subst $(eval) ,;,$(wildcard $1))

libs := $(wildcard $(lib)/*.jar)
libs_decanted := $(libs:$(lib)/%.jar=$(obj)/%.decanted)

src_files := $(wildcard $(src)/*.java)
artifacts := $(src_files:$(src)/%.java=$(out)/%.class)

javac_args := -cp "$(src);$(call classpathify,$(libs))" -d $(out) -g -parameters

obj_dirs   = $(wildcard $(obj)/*)
java_args  = -cp "$(call classpathify,$(obj_dirs))"

default:
	@echo libs = $(libs)
	@echo Please choose a target

jar: $(jar)
	

$(obj)/%.decanted: $(lib)/%.jar
	7z x -y -o$@ $<

$(out)/%.class: $(src)/%.java
	@echo ----- MAK $< -----
	-javac $(javac_args) $<

$(jar): $(artifacts)
	jar cvfm $@ Manifest.txt$(foreach dir,$(obj_dirs), -C $(dir) .)

run~%: $(artifacts) $(libs_decanted)
	$(eval J=$(notdir $(patsubst run~%,%,$@)))
	@echo ----- RUN $(J) -----
	java $(java_args) $(J)
	@echo ----- END $(J) -----

clean:
	-rd /s /q "$(obj)"

.PHONY: default jar run~% noop~%
.SECONDARY:
