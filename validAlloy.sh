#!/bin/bash

java $(tr '\n' ' ' < src/tinylog.properties)-cp bin:lib/antlr-3.5-complete.jar:lib/alloy4.2.jar:lib/commons-io-2.4.jar:lib/tinylog.jar VallidAlloy $1

