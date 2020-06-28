# SafeToast
兼容android 7.1.1 toast崩溃问题

不用反射和编译期改代码，只需重写Application.getSystemService方法即可
```kotlin
class App : Application() {

    override fun getSystemService(name: String): Any? {
        return SafeToastService.getSystemService(name, super.getSystemService(name))
    }
}
```
