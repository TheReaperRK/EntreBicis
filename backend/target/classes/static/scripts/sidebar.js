document.addEventListener("DOMContentLoaded", function () {
    fetch('/navigation/sidebar.html')
        .then(response => response.text())
        .then(data => {
            const sidebarContainer = document.createElement("div");
            sidebarContainer.innerHTML = data;
            document.body.prepend(sidebarContainer);

            setTimeout(() => {
                const sidebar = document.getElementById("sidebar");
                const toggleButton = document.getElementById("toggle-sidebar");

                if (sidebar && toggleButton) {
                    // 💡 Control de apertura por ruta
                    const currentPath = window.location.pathname;

                    // Lista de páginas donde la barra debe iniciar cerrada
                    const closedPages = ["", ""];

                    if (closedPages.includes(currentPath)) {
                        sidebar.classList.add("sidebar-closed");
                    }

                    toggleButton.addEventListener("click", function () {
                        sidebar.classList.toggle("sidebar-closed");
                    });
                } else {
                    console.error("Error: No se encontró la barra lateral o el botón.");
                }
            }, 100);
        })
        .catch(error => console.error("Error cargando la barra lateral:", error));
});
