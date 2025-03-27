
document.addEventListener("DOMContentLoaded", function () {
    fetch('/navigation/sidebar.html')
        .then(response => response.text())
        .then(data => {
            // Crear el contenedor de la barra lateral
            const sidebarContainer = document.createElement("div");
            sidebarContainer.innerHTML = data;
            document.body.prepend(sidebarContainer); // Insertar en el body

            // Esperar un pequeño tiempo para asegurarse de que el HTML está cargado
            setTimeout(() => {
                const sidebar = document.getElementById("sidebar");
                const toggleButton = document.getElementById("toggle-sidebar");

                if (sidebar && toggleButton) {
                    toggleButton.addEventListener("click", function () {
                        sidebar.classList.toggle("sidebar-closed");
                    });
                } else {
                    console.error("Error: No se encontró la barra lateral o el botón.");
                }
            }, 100); // Esperar 100ms para asegurarse de que se inserta correctamente
        })
        .catch(error => console.error("Error cargando la barra lateral:", error));
});
