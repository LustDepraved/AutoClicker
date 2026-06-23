#!/bin/sh
if [ -n "$JAVA_HOME" ]; then
  export JAVA_HOME
fi
exec gradle "$@"
