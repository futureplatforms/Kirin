rm -rf target/bindings
mkdir target/bindings
mv target/$1-$2/app/$3/BINDINGS target/bindings
mv target/$1-$2/app/$3/SERVICE_BINDINGS target/bindings
