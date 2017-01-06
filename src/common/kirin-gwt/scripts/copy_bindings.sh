rm -rf target/bindings
mkdir target/bindings

target_app=target/$1-$2/app/$3
if [ $4 = "2.6.0" ]; then
    target_app=target/$1-$2/app
fi

mv $target_app/BINDINGS target/bindings
mv $target_app/SERVICE_BINDINGS target/bindings
mv target/$1-$2/app target
