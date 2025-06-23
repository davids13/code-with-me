#!/bin/bash

for i in {1..7}; do curl -i http://localhost:8080/api/v1/metrics/cpu; echo; sleep 0.5; done