#!/bin/bash

java -jar compiler.jar \ 
--js kendo.web.js \
--create_source_map ./kendo.web.js.map \
--js_output_file kendo.web.min.js
