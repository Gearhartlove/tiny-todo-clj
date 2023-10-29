# cli-1

A tiny Clojure todo cli

### goals
(*start learning clojure!*)
- import first library
  - toml parsing with `toml "0.1.4"`
- implement basic todo actions
  - add
  - delete
  - complete
  - list todo + completed tasks
- save + read from file 
- implement unit tests
 
### usage
(*assumes you have leiningen installed*)

`lein run add "Go to the store"`

`lein run done "Go to the store"`

`lein run remove "Go to the store"`

`lein run list`

