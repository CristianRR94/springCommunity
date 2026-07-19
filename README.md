# Spring Community

[![Documentation](https://img.shields.io/badge/docs-Mintlify-blueviolet?style=for-the-badge)](https://cristianrr94-springcommunity-7.mintlify.site)

Una API RESTful diseñada para la gestión de eventos, con funcionalidades propias de una red social.

---

## Tecnologías usadas:

*   **Backend:** Java 25, Spring Boot 4, Maven
*   **Base de Datos:** PostgreSQL, JPA / Hibernate
*   **Seguridad:** Spring Security, JSON Web Tokens (JWT)
*   **Infraestructura:** Docker, Docker Compose
*   **Testing:** JUnit 5, Mockito
*   **Documentación:** Mintlify

---

##  Características:

*   **Autenticación mediante JWT:** Arquitectura stateless mediante json-web-tokens.
*   **Estructura monolítica basada en capas:** La aplicación contiene toda la funcionalidad. La organización sigue un división de responsabilidades, no de negocio.
*   **Entorno docker:** Configuración lista para producción usando Docker para asegurar la consistencia entre entornos.

---

## Documentación completa:

La guía detallada de la API, que incluye el diseño de la arquitectura, la configuración de variables de entorno y la referencia completa de los endpoints interactivos, está disponible en:

**[Documentación completa de Spring Community](https://cristianrr94-springcommunity-7.mintlify.site)**

---

## Proyecto en local:

Levantar el proyecto en un entorno local con **Docker**:

```bash
# 1. Clonar el repositorio
git clone https://github.com/CristianRR94/springCommunity.git

# 2. Ir al directorio
cd springCommunity

# 3. Levantar los contenedores (Aplicación + Base de datos)
docker-compose up -d
