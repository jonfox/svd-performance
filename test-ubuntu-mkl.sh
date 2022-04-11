#!/bin/bash

set -eo pipefail

docker build -t svd-performance:ubuntu-mkl -f Dockerfile.ubuntu.mkl .

docker run --rm svd-performance:ubuntu-mkl
