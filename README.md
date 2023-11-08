# The Drones Application

![CI/CD Workflow](https://github.com/codecharlan/Test/actions/workflows/maven.yml/badge.svg)

## Overview

Welcome to the Drones Application, a REST API service designed to efficiently manage drones and their associated medications. Our service provides an array of capabilities:

- **Register New Drones:** Seamlessly register new drones to your fleet.
- **Load Medications onto Drones:** Prepare drones for vital missions by loading medications.
- **Retrieve Loaded Medication Information:** Access detailed information about medications loaded onto a specific drone.
- **List All Drones:** Get a comprehensive list of all available drones.
- **Check Drone Battery Levels:** Monitor the battery levels of individual drones.

### Prerequisites

Ensure that input and output data are formatted in JSON.

### Getting Started

To begin using the Drones Application, follow these steps:

1. Install the required dependencies by running:
   ```shell
   mvn install
    ```

2. Start the application with:
    ```shell
    mvn spring-boot:run
    ```

### API Endpoints
The API exposes the following endpoints:

* **Register a New Drone:** Send a POST request to **`/drones/register`** with a JSON request body including serial number and model.

* **Load Medications onto a Drone:** Issue a POST request to **`/drones/load-medic/{serialNumber}`** and provide a JSON request body containing medication details.

* **Check Loaded Medication Items:** Retrieve information about loaded medications for a specific drone with a GET request to **`/drones/loaded-medic/{serialNumber}`**.

* **List All Drones:** Obtain a list of all drones with a GET request to **`/drones/all-drones`**.

* **Check Available Drones:** Discover available drones for loading with a GET request to **`/drones/available`**.

* **Check Drone Battery Level:** Monitor the battery level of a specific drone by sending a GET request to **`/battery-level/{serialNumber}`**.

For comprehensive API usage details, refer to our [Postman Documentation](https://documenter.getpostman.com/view/29888943/2s9YXh532Q).


### Assumptions
In our drone management system, we've made several key assumptions to model drone behavior:
Drones transition between states, including RETURNING, LOADED, DELIVERING, DELIVERED, and IDLE, with expected state transitions.
Every One Minute,
* A drone in the RETURNING state will eventually return to the base and transition to the IDLE state.
* A drone in the LOADED state will eventually start delivering its medication and transition to the DELIVERING state.
* A drone in the DELIVERING state will eventually deliver its medication and transition to the DELIVERED state.
* A drone in the DELIVERED state will eventually return to the base and transition to the RETURNING state.

Battery levels are adjusted based on state, with incremental/decremental changes as follows.
* A drone in the IDLE state will have its battery level increased by 2.60% every 12 seconds.
* A drone in the LOADING state will have its battery level decreased by 2.15% every 12 seconds.
* A drone in the LOADED state will have its battery level decreased by 4.35% every 12 seconds.
* A drone in the DELIVERING state will have its battery level decreased by 8.55% every 12 seconds.
* A drone in the DELIVERED state will have its battery level decreased by 5.75% every 12 seconds.
* A drone in the RETURNING state will have its battery level decreased by 3.40% every 12 seconds.

Real-world factors affecting battery consumption, such as drone type and payload weight, are NOT considered. These are just estimates to guide the system's functionality.

### Testing
Unit tests can be run using the following command:

```shell
    mvn test
   ```

### Technology Used:
* Java
* SpringBoot
* Docker
* CI/CD
* Junit & Mockito
* Git and GitLab
* Postman

### Conclusion
In conclusion, our REST API service offers a robust solution for the management of drones and their medications. With a set of well-defined endpoints, clients can seamlessly register new drones, 
load medications onto drones, retrieve details about loaded medications, check the availability of drones, and monitor drone battery levels. 
Our service is designed to handle various scenarios and adapt to the dynamic nature of drone operations.

It's important to emphasize that our service is based on certain assumptions that consider factors like battery levels and drone states,
but real-world conditions can significantly affect drone performance. These assumptions provide a framework for understanding how our service operates in an idealized setting.

We encourage you to explore the endpoints and functionalities provided by our API and learn more from our [Postman Documentation](https://documenter.getpostman.com/view/29888943/2s9YXh532Q)
for a detailed guide on using our service effectively. Whether you're a developer, drone operator, or part of a logistics team, our service is designed to simplify drone management 
tasks and enhance operational efficiency.

Feel free to reach out with any questions, feedback, or suggestions.