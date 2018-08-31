
function updateNetwork() {
    if (selectedNode == 1) {
        edges.add([{from: selectedNode, to: '99'}]);
        nodes.add([{id: '99', label: 'Lucarelli'}]);
    }
    if (selectedNode == 4) {
        edges.add([{from: selectedNode, to: '10'}]);
        nodes.add([{id: '5', label: 'Protti'}]);
    }
}

function draw() {
    nodes = new vis.DataSet();
    edges = new vis.DataSet();
    selectedNode = null;
    var a = document.getElementById('formnascosto:nascosto').value;
    var res = a.split("-");
    var nodeList = res[0].split("*");
    var edgeList = res[1].split("*");
    var network = null;

    // creo i nodi
    var len = nodeList.length;
    for (var i = 0; i < len; i++) {
        if (i == 0) {
            nodes.add({
                id: i,
                label: nodeList[i],
                color: 'red'
            });
        } else {
            nodes.add({
                id: i,
                label: nodeList[i]
            });
        }
    }

    //  creo le relazioni
    var len = edgeList.length;
    for (var i = 0; i < len; i++) {
        edges.add({
            from: 0,
            to: i + 1,
            label: edgeList[i]
        });
    }

   options = {
        navigation: true,
        stabilize: false,
        edges: {
            width: 2,
            style: 'arrow',
            color: 'gray'
        },
        nodes: {
            // default for all nodes
            fontFace: 'times',
            shape: 'box',
            color: {
                border: 'orange',
                background: 'yellow',
                highlight: {
                    border: 'darkorange',
                    background: 'gold'
                }
            }
        },
        physics: {
            barnesHut: {
                gravitationalConstant: -70,
                centralGravity: 0, //this should be set to 0 to avoid jumping
                springLength: 150,
                springConstant: 0, //this should be set to 0 to avoid jumping
                damping: 0.20
            },
            repulsion: {
                centralGravity: 0.1,
                springLength: 10,
                springConstant: 0.1,
                nodeDistance: 300,
                damping: 0.09
            }
        }// this is the correct way to set the length of the springs
    };
    // create the network
    var container = document.getElementById('mynetwork');
    var data = {
        nodes: nodes,
        edges: edges
    };
    network = new vis.Network(container, data, options);
    // add event listener
    network.on('select', function(properties) {
        document.getElementById('info').innerHTML += 'Hai selezionato il nodo: ' + properties.nodes + '<br/>';
        selectedNode = properties.nodes;
    });

}