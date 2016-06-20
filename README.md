# keycloak-leakage
Demonstrates keycloak leaking memory until the heap explodes

## Usage
```
cd {KEYCLOAK_PATH}/bin && ./standalone.sh

sbt run
```
![Leakage](https://raw.githubusercontent.com/anaerobic/keycloak-leakage/master/mem-cpu.png)
