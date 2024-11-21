// Include packages
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[RequireComponent(typeof(SpriteRenderer))] 
public class GhoulMove : MonoBehaviour
{
    // Define variables
    [SerializeField] private float speed;
    [SerializeField] private Path path;
    private Vector3 waypoint;
    private Vector3 direction;
    private float distanceTravelled;
    private float distanceToWaypoint;
    private int nextWaypoint = 1;

    // Define level manager
    [SerializeField] private LevelManager levelManager;

    private void OnTriggerEnter2D(Collider2D collider)
    {
        Destroy(gameObject);
    }

    private void Start()
    {
        // Initialise variables
        transform.position = path.Waypoint(0); 
        waypoint = path.Waypoint(1);
        direction = waypoint - transform.position;
    }

    private void Update()
    {
        waypoint = path.Waypoint(nextWaypoint);
        distanceTravelled = speed * Time.deltaTime;
        distanceToWaypoint = Vector3.Distance(waypoint, transform.position);

        if (distanceToWaypoint <= distanceTravelled)
        {
            transform.position = waypoint; 
            NextWaypoint();
        }
        else 
        { 
            direction = waypoint - transform.position;
            direction = direction.normalized;
            transform.Translate(direction * distanceTravelled, Space.World); 
        }

        // When the level resets, destroy all ghouls
        if (levelManager.ResetLevel)
        {
            Destroy(gameObject);
        }
    }

    private void NextWaypoint()
    {
        nextWaypoint++;
        if (nextWaypoint == path.Length) 
        {
            Destroy(gameObject);
        }
    }

    // Setter for speed
    public float Speed
    {
        set
        {
            speed = value;
        }
    }

    // Setter for Path
    public Path Path
    {
        set
        {
            path = value;
        }
    }

    // Setter for LevelManager
    public LevelManager LevelManager
    {
        set
        {
            levelManager = value;
        }
    }
}
