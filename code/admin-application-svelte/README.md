# Eventiz Svelte Admin Panel

![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Svelte](https://img.shields.io/badge/Svelte-FF3E00?style=for-the-badge&logo=svelte&logoColor=white)


## Overview
The Eventiz Svelte Admin Panel serves as a comprehensive administrative application for managing events. The panel provides real-time insights into sensor values, including graphical representations, and offers visibility into the occupancy status of the cloakroom. Additionally, it offers a centralized view of emergencies, complete with timestamps and geographical locations on a map.

One of the key functionalities of the application is the ability for event managers to broadcast announcements efficiently. This communication is facilitated through integration with a Laravel API and a websockets server. The application utilizes Svelte, a modern JavaScript framework, and is containerized for easy deployment using Docker.


## Deployment

### Development

1. Clone the repository
2. Run `npm install`
3. Run `npm run dev`

### In a Docker container

1. Clone the repository
2. Run `docker build . --no-cache -t sveltekit-docker:latest`
3. Run `docker run --name eventiz-admin -d -p 3000:3000 sveltekit-docker:latest`
4. Visit `http://localhost:3000`

## Known bugs

- None yet

## Contributors

Proudly made by:

- [Lukas Olivier](https://www.linkedin.com/in/lukas-olivier/)
- [Niels Soete](https://www.linkedin.com/in/niels-soete/)
- [Lucas Guillemyn](https://www.linkedin.com/in/lucas-guillemyn-2b060b291/)
