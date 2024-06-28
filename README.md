Task completion time: ~15h

Description:

Currency convertor allows you to exchange currency through the Euro exchange rate.
At the start, 1000 Euros are given for test transactions. When purchasing a new currency that was not yet on your account, a new card will be created that is responsible for the new currency.

A request to receive the exchange rate occurs every 5 seconds. Added error handling from the server in case of request error or Internet connection trouble.

Special conditions for fee:
1. The first 5 transactions are free.
2. Every 10th transaction is free.
3. If the entered sell amount of currency is more than 1000, the fee percentage will be 0.5% in other cases 0.7%

Main technoligies:
1. Jetpack Compose
2. MVVM + MVI
3. Retrofit
4. Room + PreferencesDataStore
5. Hilt
6. Coroutines + Kotlin Flow
7. Clean Architecture + UseCases
