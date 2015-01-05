rmdir /s /q target\bindings
md target\bindings
move target\%1-%2\app\%3\BINDINGS target\bindings
move target\%1-%2\app\%3\SERVICE_BINDINGS target\bindings
move target\%1-%2\app target