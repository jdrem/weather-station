#!/bin/bash

source /app/bin/env_secrets_expand.sh

CLASSPATH=/app:\
/app/lib/"*":\
/secrets

java -cp $CLASSPATH net.remgant.weather.Application
