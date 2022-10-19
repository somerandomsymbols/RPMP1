package com.example.myapplication;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.HashMap;
import java.util.Map;
import java.util.Comparator;
import java.util.PriorityQueue;

class Node
{
    Character ch;
    public Integer freq;
    Node left = null;
    Node right = null;

    Node(Character ch, Integer freq)
    {
        this.ch = ch;
        this.freq = freq;
    }
    public Node(Character ch, Integer freq, Node left, Node right)
    {
        this.ch = ch;
        this.freq = freq;
        this.left = left;
        this.right = right;
    }
}

class NodeComparator implements Comparator<Node> {
    public int compare(Node n1, Node n2)
    {
        return n1.freq.compareTo(n2.freq);
    }
}

public class HuffmanCoder
{
    private final Map<Character, String> codes;
    private final Map<Character, Integer> frequencies;
    private final String result;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public HuffmanCoder(String input)
    {
        if (input == null || input.length() == 0)
        {
            throw new IllegalArgumentException("Text length is 0 or text is null");
        }

        this.frequencies = new HashMap<>();

        for (int i = 0; i < input.length(); ++i)
        {
            char c = input.charAt(i);
            Integer n = this.frequencies.get(c);

            if (n == null)
                this.frequencies.put(c, 1);
            else
                this.frequencies.put(c, n + 1);
        }

        PriorityQueue<Node> pq = new PriorityQueue<>(new NodeComparator());

        for (Map.Entry<Character, Integer> entry: this.frequencies.entrySet())
        {
            pq.add(new Node(entry.getKey(), entry.getValue()));
        }

        while (pq.size() != 1)
        {
            Node left = pq.poll();
            Node right = pq.poll();

            int sum = left.freq + right.freq;
            pq.add(new Node(null, sum, left, right));
        }

        Node root = pq.peek();
        this.codes = new HashMap<>();
        encodeData(root, "", codes);
        StringBuilder sb = new StringBuilder();

        for (char c: input.toCharArray())
        {
            sb.append(codes.get(c));
        }

        this.result = sb.toString();
    }

    public Map<Character, String> getCodes()
    {
        return this.codes;
    }

    public Map<Character, Integer> getFrequencies()
    {
        return this.frequencies;
    }

    public String getResult()
    {
        return this.result;
    }

    public static void encodeData(Node root, String str, Map<Character, String> huffmanCode)
    {
        if (root == null)
        {
            return;
        }

        if (isLeaf(root))
        {
            huffmanCode.put(root.ch, str.length() > 0 ? str : "1");
        }
        encodeData(root.left, str + '0', huffmanCode);
        encodeData(root.right, str + '1', huffmanCode);
    }

    public static boolean isLeaf(Node root)
    {
        return root.left == null && root.right == null;
    }
}
