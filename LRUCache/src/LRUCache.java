import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

class Node<K,V>{
    K key;
    V data;
    Node<K,V> next;
    Node<K,V> prev;

    public Node(K key, V data){
        this.key = key;
        this.data = data;
    }
}

public class LRUCache<K,V> {

    Node<K,V> head;
    Node<K,V> tail;
    Map<K,Node<K,V>> dataMap;
    int size;
    int max;

		public LRUCache(int max){
        dataMap = new HashMap<>();
        this.max = max;
    }

    public V get(K key){
        Node<K,V> node = dataMap.get(key);
        if(node==null){
            throw new RuntimeException("No mappings found!");
        }else{
            if(node.prev != null){
                node.prev.next = node.next;
                if(node.next != null){
                    node.next.prev = node.prev;
                }else{
                    tail = tail.prev;
                }
                moveToHead(node);
            }

        }
        return node.data;
    }

    public K put(K key, V value){
        Node <K,V> current = dataMap.get(key);
        if (current != null) {
            current.data = value;
            if(current.prev != null){
                current.prev.next = current.next;
                moveToHead(current);
            }
        }else{
            if(canEvict()){
                evict();
            }
            current = new Node<K,V>(key,value);
            if(head==null || tail==null){
                head = tail = current;
            }else{
                moveToHead(current);
            }
            size++;
        }
        dataMap.put(key,current);
        return key;
    }

    private void evict(){
            if(head==null || tail==null){
                throw new RuntimeException("Cache is empty!");
            }
            remove(tail.key);
    }

    public V remove(K key){
        if(head==null || tail==null){
            throw new RuntimeException("Cache is empty!");
        }

        Node<K,V> node = dataMap.remove(key);

        if(node == null){
            throw new RuntimeException("No mappings found!");
        }

        V value = node.data;

        if(node.prev != null){
            node.prev.next = node.next;
        }else{
            head = head.next;
        }
        if(node.next != null){
            node.next.prev = node.prev;
        }else{
            tail = tail.prev;
        }

        size--;
        return value;
    }


    private void moveToHead(Node<K,V> node){
          node.prev = null;
          node.next = head;
          head.prev = node;
          head = head.prev;
    }

    public int size(){
        return size;
    }

    public boolean isEmpty(){
        return size==0;
    }

    public boolean canEvict(){
            return size==max;
    }

    public static void main(String[] args) {
        LRUCache<String,String> cache = new LRUCache<>(5);
        cache.put("name","rahul");
        cache.put("country","India");
        cache.put("city","Bangalore");
        cache.put("profession","sde");
        System.out.println(cache);
        cache.put("test","test1");
        System.out.println(cache);
        cache.put("test","test2");
        cache.put("test1","test3");
        System.out.println(cache);
        cache.get("country");
        System.out.println(cache);
    }
}