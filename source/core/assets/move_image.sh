#!/usr/bin/env bash

# Ensure that two arguments are provided.
if [ "$#" -ne 3 ]; then
    echo "Usage: $0 path arg1 arg2"
    exit 1
fi

# Assign arguments to variables for easier reference.
replace_path=$1
search_string=$2
replacement_string=$3

# Find all .java and .json files in the current directory and its subdirectories.
# For each file, use sed to replace all instances of arg1 with arg2.
find $replace_path -type f \( -name "*.java" -o -name "*.json" \) -exec sed -i'' -e "s|${search_string}|${replacement_string}|g" {} +



