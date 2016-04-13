# Magento 2 Gatling benchmark scenario
The scenario emulates concurrent requests to Magento 2 store pages. The simulation consists of the following steps:

- Visit Home Page ->
- Visit Woman Pants category ->
- Visit Overnight Duffle product ->
- Add to cart Overnight Duffle product ->
- Search for “band” keyword ->
- Go to login page -> 
- Log in ->
- Go to My Account page

## Prerequisites
- [Gatling](http://gatling.io/) 2.1.7+
- Magento 2 with standard sample data installed.

## Installation 
- Create a new directory for the scenario `[gatling_root_directory]/user-files/simulations/m2_benchmark`.
- Put the `General.scala` file to the newly created directory.
- Open `General.scala` with a text editor and change `val baseUrl` value to your own.
- Run Gatling and you should see the scenario named as `m2_benchmark.General`.
- Choose the mentioned scenario and enjoy.