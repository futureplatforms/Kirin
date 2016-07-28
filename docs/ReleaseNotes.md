### Kirin Release Notes
#### 2.0.3
- Test server has ability to fall back to real HTTP
- iOS Databases now run off the main UI thread
- Added documentation for correct threading on Kirin modules
- Some changes to ensure Kirin iOS lib is debuggable (removed `GCC_OPTIMISATION_LEVEL = 0` and `COPY_PHASE_STRIP = NO` from project)

#### 2.0.2
- Added ability to enable Kirin JS logging at runtime on iOS
- More exceptions caught from Android network read

#### 2.0.1
- Documentation formatting improvements
- `AndroidNetwork` now uses `java.net.HttpUrlConnection` instead of `org.apache.http`.
- Android database queries now happening asynchronously

#### 2.0.0
- iOS library using ARC
- iOS memory leaks fixed by using weak references to native objects
- 