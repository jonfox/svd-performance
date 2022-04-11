# SVD performance testing

Four implementations are tested:

* Luhenry's [netlib](https://github.com/luhenry/netlib)
* Fommil's [netlib-java](https://github.com/fommil/netlib-java)
* [OjAlgo](https://www.ojalgo.org/)
* Apache [Commons Math](https://commons.apache.org/proper/commons-math/)

Note Luhenry's library is the successor to Fommil & adds a few extras such as supporting the Java VectorAPI.

The tests are packaged in Docker images based on Alpine and Ubuntu.

The tests can be built & executed by the `test.sh` script.

Note we are not using native libs with Fommil's library. It's not clear why it's not picking up the system_native
library.
