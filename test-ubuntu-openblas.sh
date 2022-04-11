#!/bin/bash

set -eo pipefail

docker build -t svd-performance:ubuntu-openblas -f Dockerfile.ubuntu.openblas .

docker run --rm svd-performance:ubuntu-openblas
