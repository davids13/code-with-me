#!/bin/bash

for i in {1..4}; do curl -i -H "X-Tenant-ID: tenant-a" http://localhost:8080/api/v1/metrics/memory; echo; done