// Include packages
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[RequireComponent(typeof(SpriteRenderer))] 
public class Spellbolt : MonoBehaviour
{
    // Define spellbolt variables
    private float speed;
    private Vector3 direction;

    // Define level manager
    [SerializeField] private LevelManager levelManager;

    private void OnTriggerEnter2D(Collider2D collider)
    {
        Destroy(gameObject);
    }

    private void Update()
    {   
        // When level resets, destroy all spellbolts
        if (levelManager.ResetLevel)
        {
            Destroy(gameObject);
        }
        else
        {
            // If a border is hit, then spellbolt destroys itself
            if ((transform.position.x <= levelManager.Borders[2]) || (transform.position.x >= levelManager.Borders[3]))
            {
                Destroy(gameObject);
            }
            else if ((transform.position.y <= levelManager.Borders[1]) || (transform.position.y >= levelManager.Borders[0]))
            {
                Destroy(gameObject);
            }
            else 
            {
                // Move in the direction that the wand is pointed towards
                transform.position = Vector3.MoveTowards(transform.position, (transform.position + direction), (speed * Time.deltaTime));
            }
        }
    }

    // Getter and setter for speed
    public float Speed
    {
        set
        {
            speed = value;
        }
    }

    // Getter and setter for Direction
    public Vector3 Direction
    {
        set 
        {
            direction = value;
        }
    }

    // Getter and setter for LevelManager
    public LevelManager LevelManager
    {
        set
        {
            levelManager = value;
        }
    }
}
