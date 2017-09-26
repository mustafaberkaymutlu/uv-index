# UV Index

A simple ultraviolet index viewer app for demonstrating Instant Apps + Kotlin + Dagger 2 + MVP

## Getting Started

### Prerequisites

- Android Studio 3.0 Beta 6

### Running the App

A step by step series of examples that tell you have to get a development env running

- Sign up to [Weatherbit](https://www.weatherbit.io/) (they also have a free plan)
- Get your API key
- Put your API key to the `data.Services.API_KEY` constant inside the `base` module

That's it. You should be able to run the project. 

# TODO

* Get `AndroidInjection` in Dagger 2 working
* Referencing `:base` module's colors inside `:query` module fails after rebuilding

## Built With

* [Android Instant Apps](https://developer.android.com/topic/instant-apps/index.html)
* Kotlin
* [Mosby](https://github.com/sockeqwe/mosby) for MVP
* [Dagger 2](https://google.github.io/dagger/) for Dependency Injection

## License

    Copyright 2017 Mustafa Berkay Mutlu.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
