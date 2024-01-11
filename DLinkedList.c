#include <stdlib.h>
#include <stdio.h>

struct node{
    int data;
    struct node *next;
    struct node *prev; 
};

// insert after specific node
void insAft(struct Node* previous, int data){
    if (previous == NULL) {
        printf("Previous node cannot be null!");
        return;
    }

    struct Node* newNode = new Node;

    newNode->data = data;
    newNode->next = previous->next;
    previous->next = newNode;

    newNode->prev = previous;

    if (newNode->next != NULL)
        newNode->next->prev = newNode;
}

// insert at the beginning
void instFr(struct Node** head, int data){
    struct Node* newNode = new Node;

    newNode->data = data;
    newNode->next = (*head);
    newNode->prev = NULL;

    if ((*head) != NULL)
        (*head)->prev = newNode;

    (*head) = newNode;
}

int main(){
    struct node *head;
    struct node *one = NULL;
    struct node *two = NULL;
    struct node *three = NULL;

    one = malloc(sizeof(struct node));
    two = malloc(sizeof(struct node));
    three = malloc(sizeof(struct node));

    one->data = 1;
    two->data = 2;
    three->data = 3;

    one->next = two;
    one->prev = NULL;

    two->next = three;
    two->prev = one;

    three->next = NULL;
    three->prev = two;

    // first node
    head = one;

    return 0;
};