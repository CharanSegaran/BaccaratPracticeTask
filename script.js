//Fetch CSV content and populate the table
fetch('game_history.csv')
    .then(response => response.text())
    .then(data => {
        const table = document.getElementById('csvTable');
        const rows = data.trim().split('\n');
        rows.forEach(row => {
            const cells = row.split(',');
            const tr = document.createElement('tr');
            cells.forEach(cell =>{
                const td = document.createElement('td');
                //Add class to cells based on their content
                if (cell.trim() == 'D') {
                    td.classList.add('green');
                } else if (cell.trim() == 'P') {
                    td.classList.add('red');
                } else if (cell.trim() == 'B') {
                    td.classList.add('blue');
                }
                td.textContent = cell;
                tr.appendChild(td);
            });
            table.appendChild(tr);
        });        
    })
    .catch(error => console.error('Error fetching csv', error));