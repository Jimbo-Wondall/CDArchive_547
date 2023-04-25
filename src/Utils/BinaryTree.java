package Utils;

public class BinaryTree {
    Node rootNode;

    public BinaryTree(Object initialValue, String valueType){
        rootNode = new Node(initialValue, valueType, null);
    }

    public void add(Object value){
        rootNode.add(value);
    }

    public void remove(Object value){
        Node node = rootNode.find(value);
        if (node == null){
            System.out.println("Could not find requested value");
        }
        else{
            node.remove();
        }
    }

    public void rebalanceTree(){

    }

    class Node {
        Node left = null;
        Node right = null;
        Node parent;
        Object value;
        String treeType;

        public Node(Object inputValue, String valueType, Node inParent){
            value = inputValue;
            treeType = valueType;
            parent = inParent;
        }

        public Node find(Object value){
            Node output = null;
            if (left != null){
                output = left.find(value);
            }
            else if (right != null && output == null){
                output = right.find(value);
            }
            return output;
        }

        public void remove(){
            if (left != null){
                value = left.value;
                if (left.left == null && left.right == null){
                    left = null;
                } else {
                    left.remove();
                }
            } else if (right != null) {
                value = right.value;
                if (right.left == null && right.right == null){
                    right = null;
                } else {
                    right.remove();
                }
            } else {
                // error
            }
        }

        public void add(Object inputValue){
            if (compare(inputValue, value)){
                if (right == null){
                    right = new Node(value, treeType, this);
                }
                else{
                    right.add(value);
                }
            }
            else{
                if (left == null){
                    left = new Node(value, treeType, this);
                }
                else{
                    left.add(value);
                }
            }
        }

        private boolean compare(Object obj1, Object obj2){
            if (treeType.equals("int")){
                return ((int)obj1 > (int)obj2);
            }
            else if (treeType.equals("String")){
                String str1 = ((String)obj1).toLowerCase();
                String str2 = ((String)obj2).toLowerCase();
                return (str1.compareTo(str2) > 0);
            }
            return false;
        }
    }
}
