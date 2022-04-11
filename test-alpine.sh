#!/bin/bash

set -eo pipefail

docker build -t svd-performance:alpine -f Dockerfile.alpine .

docker run --rm svd-performance:alpine
