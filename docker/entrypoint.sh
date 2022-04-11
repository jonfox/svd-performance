#!/bin/bash

echo "Running ${IMAGE} image"
exec "${JAVA_HOME}"/bin/java \
  -Xmx4g \
  -Dcom.github.fommil.netlib.BLAS=com.github.fommil.netlib.NativeSystemBLAS \
  -Dcom.github.fommil.netlib.LAPACK=com.github.fommil.netlib.NativeSystemLAPACK \
  -cp "${SERVER_HOME}/lib/*" "Program"
