# Eventiz Laravel Api

![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Laravel](https://img.shields.io/badge/Laravel-FF2D20?style=for-the-badge&logo=laravel&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)

## Overview

The Eventiz Laravel API and Websockets Server serve as the communication backbone for the entire Eventiz ecosystem. This robust system manages data exchange between different applications, providing a centralized hub for real-time updates. The API also plays a crucial role in triggering actions in response to computed events, such as raising alarms when sensor values reach dangerous levels for an extended period. All data is securely stored in a MySQL database.

## Deployment

1. Clone the repository
2. Duplicate the .env.example file, choose your database credentials and save it as .env 
3. Launch a terminal in the project folder and run `sudo DOCKER_BUILDKIT=0 docker-compose up --build -d`
4. Lets configure our Laravel project. Run `docker exec -it eventiz-app sh` to enter the container.
5. Install the necessary dependencies by running `composer update --ignore-platform-req=ext-http`
6. Next, run `php artisan key:generate` to generate an application key.
7. Run `php artisan migrate` to migrate the database.
8. Run `php artisan db:seed` to seed the database.
9. The application should be running on `http://localhost:8393`


## API Documentation

You can find the API documentation [here](https://gitlab.ti.howest.be/ti/2023-2024/s5/ccett/projects/group12/code/laravel/-/blob/main/openapi.yaml?ref_type=heads)

## Known bugs

-   None yet

## Contributors

Proudly made by:

-   [Lukas Olivier](https://www.linkedin.com/in/lukas-olivier/)
-   [Niels Soete](https://www.linkedin.com/in/niels-soete/)
-   [Lucas Guillemyn](https://www.linkedin.com/in/lucas-guillemyn-2b060b291/)
